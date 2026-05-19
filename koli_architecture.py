import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch

fig, ax = plt.subplots(1, 1, figsize=(20, 14))
fig.patch.set_facecolor('#0F1117')
ax.set_facecolor('#0F1117')
ax.set_xlim(0, 20)
ax.set_ylim(0, 14)
ax.axis('off')

# ── Color palette ──────────────────────────────────────────────
C_LAYER_BG   = '#1A1D2E'
C_BORDER     = '#2E3250'
C_UI         = '#1E3A5F'
C_BORDER_UI  = '#4A90D9'
C_VM         = '#1A3A2A'
C_BORDER_VM  = '#4CAF50'
C_REPO       = '#3A2A1A'
C_BORDER_RP  = '#FF9800'
C_DATA       = '#2A1A3A'
C_BORDER_DT  = '#9C27B0'
C_EXT        = '#3A1A1A'
C_BORDER_EX  = '#F44336'
C_ML         = '#1A2A3A'
C_BORDER_ML  = '#00BCD4'
C_ARROW      = '#4A4E6A'
C_INTENT     = '#FFD700'
C_TEXT_HEAD  = '#FFFFFF'
C_TEXT_SUB   = '#B0B8D0'
C_TEXT_ITEM  = '#D0D8F0'

def layer_box(x, y, w, h, color, border, label, label_color='#FFFFFF'):
    rect = FancyBboxPatch((x, y), w, h,
                          boxstyle="round,pad=0.08",
                          facecolor=color, edgecolor=border, linewidth=1.8, zorder=2)
    ax.add_patch(rect)
    ax.text(x + w/2, y + h - 0.22, label,
            ha='center', va='top', fontsize=8.5, fontweight='bold',
            color=label_color, zorder=3)

def item_box(x, y, w, h, label, color, border, fontsize=7.2):
    rect = FancyBboxPatch((x, y), w, h,
                          boxstyle="round,pad=0.05",
                          facecolor=color, edgecolor=border, linewidth=1.2, zorder=4)
    ax.add_patch(rect)
    ax.text(x + w/2, y + h/2, label,
            ha='center', va='center', fontsize=fontsize,
            color=C_TEXT_ITEM, zorder=5, wrap=True)

def arrow(x1, y1, x2, y2, color=C_ARROW, style='->', lw=1.4):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle=style, color=color,
                                lw=lw, connectionstyle='arc3,rad=0.0'),
                zorder=6)

def intent_arrow(x1, y1, x2, y2, label=''):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=C_INTENT,
                                lw=1.8, linestyle='dashed',
                                connectionstyle='arc3,rad=0.15'),
                zorder=6)
    if label:
        mx, my = (x1+x2)/2, (y1+y2)/2
        ax.text(mx, my, label, ha='center', va='center', fontsize=6,
                color=C_INTENT, zorder=7,
                bbox=dict(boxstyle='round,pad=0.1', facecolor='#0F1117', edgecolor=C_INTENT, lw=0.8))

# ════════════════════════════════════════════════════════════════
# Title
ax.text(10, 13.6, 'KoLi – Korea Life Assistant',
        ha='center', va='top', fontsize=16, fontweight='bold', color='#FFFFFF', zorder=3)
ax.text(10, 13.2, 'System Architecture  |  Android (Kotlin)  |  MVVM + Repository Pattern',
        ha='center', va='top', fontsize=9, color=C_TEXT_SUB, zorder=3)

# ════════════════════════════════════════════════════════════════
# LAYER 1 – Presentation (Activities + Fragments)
layer_box(0.3, 10.4, 19.4, 2.6, C_LAYER_BG, C_BORDER, '')
ax.text(0.6, 12.85, '① PRESENTATION LAYER', fontsize=8, fontweight='bold', color=C_BORDER_UI, zorder=3)

# Activities row
item_box(0.5,  11.7, 2.6, 0.9, 'SplashActivity',     C_UI, C_BORDER_UI)
item_box(3.3,  11.7, 3.2, 0.9, 'MainActivity\n(DrawerLayout + ViewPager2)', C_UI, C_BORDER_UI)
item_box(6.7,  11.7, 2.8, 0.9, 'CameraActivity\n(ML Kit OCR)', C_UI, C_BORDER_UI)
item_box(9.7,  11.7, 3.0, 0.9, 'PlaceDetailActivity', C_UI, C_BORDER_UI)

# Fragments row
item_box(0.5,  10.6, 2.6, 0.85, 'MapFragment',       C_UI, C_BORDER_UI)
item_box(3.3,  10.6, 2.6, 0.85, 'ChatFragment',      C_UI, C_BORDER_UI)
item_box(6.1,  10.6, 2.6, 0.85, 'FavoriteFragment',  C_UI, C_BORDER_UI)
item_box(8.9,  10.6, 2.6, 0.85, 'PhraseFragment',    C_UI, C_BORDER_UI)

# Jetpack badges
ax.text(13.0, 12.55, 'Jetpack Components:', fontsize=7, color=C_TEXT_SUB, zorder=3)
for i, jp in enumerate(['RecyclerView', 'Fragment', 'ViewPager2', 'DrawerLayout']):
    bx = 13.0 + i*1.65
    item_box(bx, 12.62, 1.5, 0.45, jp, '#1A2440', '#4A90D9', fontsize=6.5)

item_box(13.0, 10.55, 6.6, 1.8, 'Coroutines (launch / viewModelScope)\nRetrofit 2.11.0  ·  LiveData / StateFlow', '#1A2030', '#4A90D9', fontsize=7.5)

# ════════════════════════════════════════════════════════════════
# LAYER 2 – ViewModel
layer_box(0.3, 8.3, 12.2, 1.9, C_LAYER_BG, C_BORDER, '')
ax.text(0.6, 10.05, '② VIEWMODEL LAYER', fontsize=8, fontweight='bold', color=C_BORDER_VM, zorder=3)

for i, vm in enumerate(['MapViewModel', 'ChatViewModel', 'FavoriteViewModel', 'CameraViewModel']):
    item_box(0.5 + i*3.0, 8.5, 2.7, 1.3, vm, C_VM, C_BORDER_VM)

# ════════════════════════════════════════════════════════════════
# LAYER 3 – Repository
layer_box(0.3, 6.2, 12.2, 1.9, C_LAYER_BG, C_BORDER, '')
ax.text(0.6, 7.95, '③ REPOSITORY LAYER', fontsize=8, fontweight='bold', color=C_BORDER_RP, zorder=3)

for i, rp in enumerate(['ChatRepository', 'TranslateRepository', 'FavoriteRepository']):
    item_box(0.5 + i*4.0, 6.4, 3.5, 1.3, rp, C_REPO, C_BORDER_RP)

# ════════════════════════════════════════════════════════════════
# LAYER 4 – Data (Retrofit + Room)
layer_box(0.3, 3.9, 12.2, 2.1, C_LAYER_BG, C_BORDER, '')
ax.text(0.6, 5.78, '④ DATA LAYER', fontsize=8, fontweight='bold', color=C_BORDER_DT, zorder=3)

# Retrofit
item_box(0.5, 4.1, 5.6, 1.6, 'Retrofit Client\n─────────────────────\nOpenAiApi  ·  TranslateApi\nRetrofitClient (OkHttp)', C_DATA, C_BORDER_DT)
# Room
item_box(6.3, 4.1, 5.9, 1.6, 'Room Database (AppDatabase)\n─────────────────────\nFavoritePlaceDao/Entity\nChatHistoryDao/Entity', C_DATA, C_BORDER_DT)

# ════════════════════════════════════════════════════════════════
# LAYER 5 – External APIs
layer_box(0.3, 1.3, 12.2, 2.35, C_LAYER_BG, C_BORDER, '')
ax.text(0.6, 3.42, '⑤ EXTERNAL SERVICES', fontsize=8, fontweight='bold', color=C_BORDER_EX, zorder=3)

item_box(0.5,  1.5, 3.6, 1.6, 'Google Maps SDK\nPlaces API (New) v4.1.0\n──────────────────\ngoogle cloud console', C_EXT, C_BORDER_EX)
item_box(4.3,  1.5, 3.6, 1.6, 'OpenAI API\ngpt-3.5-turbo\n──────────────────\nplatform.openai.com', C_EXT, C_BORDER_EX)
item_box(8.1,  1.5, 4.1, 1.6, 'Google Cloud\nTranslation API v2\n──────────────────\ngoogle cloud console', C_EXT, C_BORDER_EX)

# ════════════════════════════════════════════════════════════════
# ML Kit (right side panel)
layer_box(13.0, 1.3, 6.5, 4.7, C_LAYER_BG, C_BORDER, '')
ax.text(13.3, 5.78, '⑥ ML (ON-DEVICE)', fontsize=8, fontweight='bold', color=C_BORDER_ML, zorder=3)

item_box(13.2, 4.1, 6.0, 1.6,
         'ML Kit Text Recognition\n(Korean)\n─────────────────────────\nOn-device · No internet needed\nCameraX 1.4.1 preview input',
         C_ML, C_BORDER_ML)
item_box(13.2, 1.5, 6.0, 2.3,
         'Flow\n──────────────────────\nCameraX → ImageProxy\n→ InputImage\n→ TextRecognizer\n→ CameraViewModel\n→ CameraActivity result',
         C_ML, C_BORDER_ML, fontsize=7)

# ════════════════════════════════════════════════════════════════
# Intent arrows (golden dashed)
intent_arrow(4.7, 11.7, 4.7, 10.2,  '')   # MainActivity → Fragments (straight down)
intent_arrow(3.3, 12.15, 6.7, 12.15, 'Intent ①\nlangCode')   # Main → Camera
intent_arrow(6.7, 11.9,  6.4, 11.9,  'Intent ②\nsetResult')  # Camera → Main
intent_arrow(9.7, 10.6, 10.2, 10.1, 'Intent ③\nPlace data')  # Map → PlaceDetail

# Vertical layer arrows (center)
for y_from, y_to in [(10.4, 10.22), (8.3, 8.15), (6.2, 5.95), (3.9, 3.6)]:
    arrow(6.0, y_from, 6.0, y_to, color='#5A6080', lw=1.2)

# ════════════════════════════════════════════════════════════════
# Legend
legend_items = [
    (C_UI,    C_BORDER_UI, 'UI / Activities / Fragments'),
    (C_VM,    C_BORDER_VM, 'ViewModel'),
    (C_REPO,  C_BORDER_RP, 'Repository'),
    (C_DATA,  C_BORDER_DT, 'Data (Retrofit + Room)'),
    (C_EXT,   C_BORDER_EX, 'External API'),
    (C_ML,    C_BORDER_ML, 'ML Kit (on-device)'),
]
lx, ly = 0.5, 1.0
for i, (fc, ec, label) in enumerate(legend_items):
    rect = FancyBboxPatch((lx + i*3.15, ly - 0.22), 0.28, 0.22,
                          boxstyle="round,pad=0.02",
                          facecolor=fc, edgecolor=ec, linewidth=1.0, zorder=5)
    ax.add_patch(rect)
    ax.text(lx + i*3.15 + 0.35, ly - 0.11, label, va='center',
            fontsize=6.5, color=C_TEXT_SUB, zorder=5)

ax.plot([lx + 6*3.15 - 0.3, lx + 6*3.15 + 0.15], [ly - 0.11, ly - 0.11],
        color=C_INTENT, linestyle='--', lw=1.5, zorder=5)
ax.text(lx + 6*3.15 + 0.25, ly - 0.11, 'Intent flow', va='center',
        fontsize=6.5, color=C_TEXT_SUB, zorder=5)

# ════════════════════════════════════════════════════════════════
plt.tight_layout(pad=0.2)
out = r'C:\Users\falli\AndroidStudioProjects\swtermproject\koli_architecture.jpg'
plt.savefig(out, dpi=180, bbox_inches='tight',
            facecolor=fig.get_facecolor(), format='jpg', pil_kwargs={'quality': 95})
print('Saved:', out)
