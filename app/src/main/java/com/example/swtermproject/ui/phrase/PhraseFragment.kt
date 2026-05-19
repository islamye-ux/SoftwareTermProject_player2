package com.example.swtermproject.ui.phrase

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtermproject.R
import com.example.swtermproject.databinding.FragmentPhraseBinding
import com.example.swtermproject.databinding.ItemPhraseBinding
import androidx.recyclerview.widget.RecyclerView

class PhraseFragment : Fragment() {

    data class Phrase(val category: String, val korean: String, val english: String)

    private val phrases = listOf(
        Phrase("Emergency", "도와주세요!", "Please help me!"),
        Phrase("Emergency", "119에 전화해 주세요", "Please call 119 (ambulance)"),
        Phrase("Emergency", "경찰을 불러주세요", "Please call the police"),
        Phrase("Hospital", "아파요", "I don't feel well"),
        Phrase("Hospital", "약을 주세요", "Please give me medicine"),
        Phrase("Hospital", "영어를 할 수 있는 의사가 있나요?", "Is there an English-speaking doctor?"),
        Phrase("Transport", "지하철역이 어디예요?", "Where is the subway station?"),
        Phrase("Transport", "택시 불러주세요", "Please call a taxi"),
        Phrase("Transport", "공항에 가주세요", "Please take me to the airport"),
        Phrase("Food", "이거 주세요", "I'll have this one"),
        Phrase("Food", "계산해 주세요", "Check, please"),
        Phrase("Food", "얼마예요?", "How much is this?"),
        Phrase("Shopping", "너무 비싸요", "It's too expensive"),
        Phrase("Shopping", "할인해 주세요", "Can you give me a discount?"),
        Phrase("Government", "비자 연장하고 싶어요", "I want to extend my visa"),
        Phrase("Government", "외국인 등록증을 신청하고 싶어요", "I want to apply for an alien registration card"),
        Phrase("Government", "건강보험에 가입하고 싶어요", "I want to sign up for health insurance")
    )

    private var _binding: FragmentPhraseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPhraseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPhrases.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PhraseAdapter(phrases) { korean ->
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Korean phrase", korean))
                Toast.makeText(requireContext(), R.string.copied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class PhraseAdapter(
        private val items: List<Phrase>,
        private val onCopy: (String) -> Unit
    ) : RecyclerView.Adapter<PhraseAdapter.VH>() {

        inner class VH(private val b: ItemPhraseBinding) : RecyclerView.ViewHolder(b.root) {
            fun bind(phrase: Phrase) {
                b.tvCategory.text = phrase.category
                b.tvKorean.text = phrase.korean
                b.tvEnglish.text = phrase.english
                b.root.setOnClickListener { onCopy(phrase.korean) }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VH(ItemPhraseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
        override fun getItemCount() = items.size
    }
}
