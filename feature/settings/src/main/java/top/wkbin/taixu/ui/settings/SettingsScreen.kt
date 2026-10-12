package top.wkbin.taixu.ui.settings

import org.koin.compose.viewmodel.koinViewModel
import top.wkbin.taixu.ui.settings.LocalizedText as Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import top.wkbin.taixu.ui.components.IconTile
import top.wkbin.taixu.ui.components.MainDestination
import top.wkbin.taixu.ui.components.RuntimeBottomBar
import top.wkbin.taixu.ui.components.RuntimeCard
import top.wkbin.taixu.ui.components.RuntimeIcon
import top.wkbin.taixu.ui.components.RuntimeIconName
import top.wkbin.taixu.ui.components.RuntimeTopBar
import top.wkbin.taixu.ui.components.rememberRuntimeTopBarScrollState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import top.wkbin.taixu.ui.components.liquidGlassContent
import top.wkbin.taixu.ui.theme.LocalThemeStyle
import top.wkbin.taixu.ui.theme.ThemeStyle

/**
 * 太墟 · 乾坤配置 (TaiXu Settings & Models)
 */
@Composable
fun SettingsScreen(
    onNavigate: (MainDestination) -> Unit,
    onOpenAgentEco: () -> Unit,
    onOpenLinuxEnv: () -> Unit,
    onOpenAppearance: () -> Unit,
    onOpenSystemDev: () -> Unit,
    onOpenAboutCommunity: () -> Unit,
    onOpenSearch: () -> Unit = {},
    onOpenFeatureHub: () -> Unit = {},
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val models by viewModel.models.collectAsStateWithLifecycle()
    val developer by viewModel.developerMode.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val skills by viewModel.allSkills.collectAsStateWithLifecycle()
    val effectiveExecutionMode by viewModel.effectiveExecutionMode.collectAsStateWithLifecycle()
    val installedDistros by viewModel.installedDistros.collectAsStateWithLifecycle()
    val terminalFontSize by viewModel.terminalFontSize.collectAsStateWithLifecycle()

    val themeLabel = when (themeMode) {
        "light" -> "浅色"
        "dark" -> "曜石"
        else -> "跟随系统"
    }

    val appVersionName = rememberAppVersion()
    val chengming = LocalThemeStyle.current == ThemeStyle.LIQUID_GLASS
    val topBarScroll = rememberRuntimeTopBarScrollState()
    val categories = listOf(
        SettingsCategory(
            RuntimeIconName.Brain, MaterialTheme.colorScheme.primary,
            (if (chengming) "智能体与模型" else "智能体与 AI 模型"), "模型档案 · 插件工具中心 · 技能与 MCP 生态",
            if (models.isEmpty()) "未配置模型" else "${models.size} 个模型 · ${skills.count { it.isEnabled }} 技能",
            onOpenAgentEco,
        ),
        SettingsCategory(
            RuntimeIconName.Server, MaterialTheme.colorScheme.tertiary,
            (if (chengming) "容器与存储" else "Linux 容器与存储"), "多发行版管理 · 宿主存储映射 · 运行特权模式",
            "${installedDistros.size} 套系统 · ${effectiveExecutionMode.shortLabel}", onOpenLinuxEnv,
        ),
        SettingsCategory(
            RuntimeIconName.Palette, MaterialTheme.colorScheme.secondary,
            (if (chengming) "外观与显示" else "外观、字号与终端定制"), "深浅色主题 · 应用字号缩放 · 终端配色与字体",
            "$themeLabel · ${terminalFontSize}sp", onOpenAppearance,
        ),
        SettingsCategory(
            RuntimeIconName.Admin, MaterialTheme.colorScheme.primary,
            (if (chengming) "系统与诊断" else "系统保活与开发者诊断"), "后台电池优化白名单 · 调试监控 · PRoot 控制台",
            if (developer) "诊断模式已开启" else "运行平稳", onOpenSystemDev,
        ),
        SettingsCategory(
            RuntimeIconName.Sparkles, MaterialTheme.colorScheme.tertiary,
            "特色功能", "一句话做 App · AI 圆桌 · 晨报哨兵 · WebChat 大屏 · A2UI 界面",
            "5 项功能", onOpenFeatureHub,
        ),
        SettingsCategory(
            RuntimeIconName.Community, MaterialTheme.colorScheme.secondary,
            (if (chengming) "关于太墟" else "关于、更新与官方社区"), "检查新版本 · GitHub 开源仓库 · 官方 QQ 交流群",
            if (appVersionName == "unknown") "版本号未知 · 稳定版" else "v$appVersionName 稳定版",
            onOpenAboutCommunity,
        ),
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            RuntimeTopBar(
                title = "太墟 · 乾坤",
                statusText = if (chengming) null else "系统设置与控制中枢",
                largeTitle = chengming, scrollState = topBarScroll,
            )
        },
        bottomBar = {
            if (!chengming) {
                RuntimeBottomBar(MainDestination.Settings, onNavigate)
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .liquidGlassContent().nestedScroll(topBarScroll.nestedScrollConnection)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(start = if (chengming) 20.dp else 16.dp, end = if (chengming) 20.dp else 16.dp, top = 10.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 全局功能搜索快捷入口卡片
            item {
                RuntimeCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onOpenSearch,
                    containerColor = if (chengming) MaterialTheme.colorScheme.surfaceContainerHigh
                        else MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.55f),
                    borderColor = if (chengming) Color.Transparent else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 11.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        RuntimeIcon(
                            name = RuntimeIconName.Search,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "搜索全局功能与设置选项...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                            modifier = Modifier.weight(1f),
                        )
                        if (!chengming) RuntimeIcon(
                            name = RuntimeIconName.ChevronRight,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        )
                    }
                }
            }

            item { LocalBackupSettingsCard() }
            item {
                Text(
                    text = "系统与配置分类",
                    style = if (chengming) MaterialTheme.typography.bodySmall else MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp),
                )
            }

            if (chengming) {
                item { SettingsCategoryList(categories) }
            } else {
                items(categories) { category ->
                    SettingsCategoryCard(
                        icon = category.icon,
                        accent = category.accent,
                        title = category.title,
                        subtitle = category.subtitle,
                        badge = category.badge,
                        onClick = category.onClick,
                    )
                }
            }
        }
    }
}

/**
 * 现代高质感大类导航卡片（紧凑精致）
 */
@Composable
private fun SettingsCategoryCard(
    icon: RuntimeIconName,
    accent: Color,
    title: String,
    subtitle: String,
    badge: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RuntimeCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            IconTile(
                icon = icon,
                size = 36.dp,
                color = accent,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(4.dp),
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium),
                        color = accent,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp),
                    )
                }
            }

            RuntimeIcon(
                name = RuntimeIconName.ChevronRight,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
        }
    }
}
