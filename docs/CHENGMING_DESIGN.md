# 澄明设计系统：Cupertino 基础控件与局部玻璃

## 设计与实现

澄明通过 `compose-cupertino:0.1.0-alpha04` 提供基础控件；项目的 `Runtime*` 入口保持统一，
按 `LocalThemeStyle` 选择 Cupertino 或 Material 实现。业务页面不直接依赖 Cupertino 类型。

| 入口/区域 | 澄明实现 |
| --- | --- |
| 普通内容容器 | CupertinoSurface；不透明语义底色，调用者的 tint 合成到稳定底板 |
| 分组列表 | 共享内容表面，CupertinoHorizontalDivider 管理内缩分隔线，末行无分隔线 |
| 按钮、文本按钮、图标按钮 | CupertinoButton；导航/浮动操作显式启用局部 Backdrop |
| 开关、复选框、滑块 | CupertinoSwitch、CupertinoCheckBox、CupertinoSlider |
| 输入框、搜索框 | CupertinoTextField；保留键盘动作、密码转换、只读、错误、前后缀与辅助文字 |
| 分段选择、下拉菜单 | CupertinoSegmentedControl、CupertinoDropdownMenu、MenuAction |
| 圆形进度 | CupertinoActivityIndicator；减少动态效果时使用静态状态指示 |
| 线性进度、徽标、内容图标 | 普通进度轨道、实色状态/图标；不做装饰性玻璃折射 |
| 顶部导航 | CupertinoTopAppBar；独立页面滚动状态控制大标题收缩，操作按钮单独使用玻璃 |
| 底部导航 | 四个稳定目的地，共享局部玻璃容器；减少透明度后仍保留全部入口 |
| 任意内容的弹窗 | Cupertino 外观适配器：自适应有限高度约束正文、独立操作区、可用窗口/IME 高度约束（允许正文内嵌滚动列表/LazyColumn，杜绝无限高度测量崩溃） |
| 模态抽屉 | 自动适配 16dp 顶圆角与 36dp x 5dp 原生药丸拖拽手柄，采用普通 Cupertino 内容表面 |
| 底部操作表 (ActionSheet) | RuntimeActionSheet：澄明下浮动卡片双层结构、发丝分割线与标红破坏性操作；玄同下无缝降级 |
| 筛选 Chip / 单选标记 | 单选列表项接入 iOS 原生 Checkmark 对勾，FilterChip 保留语义/状态契约普通表面 |

设置、聊天、工作区、工作流、开发者、引导等页面的输入框、菜单、抽屉与选择控件统一
接到 Runtime 入口。首页、设置、工作区的大标题连接各自的滚动状态，不共享偏移。

## 材质与兼容边界

- 页面底、内容底、正文/代码/日志保持不透明；玻璃不随主题切换自动扩散到内容层。
- `RuntimeCard` 默认为 Content，Glass 仅用于显式浮动导航/操作。禁止玻璃叠玻璃。
- `LocalThemeStyle` 决定设计风格；Backdrop 的有无只决定效果能否绘制。
- 壁纸继续作为独立操作控件取样源。底部导航读取独立的页面记录层，避免递归合成。
- Cupertino 和 Material 的内容颜色、文字样式通过适配器连接；保留 Android 字体和中文零字距。
- `ChengmingIndication` 使用现代 Compose 的 IndicationNodeFactory，修复该库的旧 Indication
  与新 clickable/selectable 的运行时冲突；提供按压和键盘焦点反馈，不使用 Material 波纹。
- 不引入 UIKit native 或 Decompose；常用符号直接使用 Cupertino 库，14 个扩展符号按实际使用范围纳入源码，见 [符号版权与来源](CHENGMING_SYMBOLS_NOTICE.md)。导航和 Android 系统返回契约保持现有实现。

## 无障碍

外观设置提供减少透明度、增强对比度、减少动态效果三个持久化选项，经过
AppearancePreferences 领域分面读写，单项写入不会覆盖其他开关。

- 减少透明度：玻璃改用实色内容表面，主题身份和四个底部目的地保持一致。
- 增强对比度：文字采用黑/白，强化轮廓与容器边界，并跟随系统高对比度文字设置。
- 减少动态效果：关闭开关弹性、分段指示动画与装饰性呼吸；进度保留可感知状态。
  系统动画缩放关闭时自动生效。大标题仍跟随用户主动滚动，无额外弹性动画。
- 交互目标至少 48dp；库控件继续负责输入、滑块步进/键盘/RTL等基础契约。
- 分类、标题、表单正文允许随字号增高；弹窗正文滚动，确认/取消动作保持独立可达。

## 验证

- Robolectric 控件运行测试：真实加载 Cupertino 库，验证基础交互、菜单/顶栏运行兼容、
  关闭玻璃后的四个导航目的地、减少动态效果下的选择状态、长弹窗与大标题滚动收缩。
- 同一套测试提供 Android instrumentation 版本，以及浅色、深色、200% 字号/高对比度截图场景。
- 全项目 `test` 回归已通过；澄明新增 6 项控件运行测试全部通过，架构门禁覆盖 32 个模块。
- Debug APK 与包含 7 项场景的 Android 测试包完成编译；设备测试包编译不代表真机测试通过。
- 不透明语义色的 22 组前景/背景配对已核验普通文字对比度达到 4.5:1；局部玻璃需按实际取样背景验收。
- 本次真机安装测试 APK 被手机系统拒绝（INSTALL_FAILED_ABORTED: User rejected permissions）；
  该结果不能记为真机测试通过。未绕过系统授权，也未调用付费模型或写入真实凭证。

## 参考

- [Apple Meet Liquid Glass](https://developer.apple.com/videos/play/wwdc2025/219/)：内容、导航、操作层级。
- [Compose Cupertino](https://github.com/alexzhirkevich/compose-cupertino)：基础组件与主题；依赖采用 Maven Central 发布版本。
- [Calf](https://github.com/MohamedRejeb/Calf)：平台适配与局部玻璃控件参考。
- [社区 Figma UI Kit](https://www.figma.com/design/v2pN3KclU8mPG9piUKKgDq?node-id=15-0)：
  普通 Row（6159:482）与顶部 Toolbar（6159:448），用于核对分隔线归属、标题和操作插槽。
  社区 API 描述不作为 Apple 官方规范的事实来源。

## 2026-10-12 视觉修正

根据四个真机截图修正布局：展开标题与操作处于同一行，不保留空白紧凑顶栏；
紧凑顶栏背景显式继承页面底色，避免库默认 tertiarySystemBackground 形成灰色带。
中文页面标题去掉重复品牌前缀，采用 28sp / 35sp SemiBold；内容与标题采用同一 20dp 外边距。

设置分类采用单行分组列表，当前状态置于行尾；聊天快捷操作合并为带内缩分隔线的列表。
首页内存/存储采用共享表面摘要，保留真实指标；输入区采用普通白色内容表面。
用户聊天气泡采用 iOS Messages 经典系统蓝底色与纯白文字，配合不对称气泡圆角（18dp / 4dp 尾巴），内部 mention 标签自适应纯白半透明底板。
底部导航采用 Cupertino 空闲轮廓/选中填充符号，弱化选中底色与标签尺寸；
导航按钮以 36dp 视觉圆面配合 48dp 交互区域呈现。

底部抽屉与表单深度对齐：模态抽屉（RuntimeModalBottomSheet）在澄明下统一 16dp 顶圆角与 36dp x 5dp 原生药丸拖拽手柄；
新增 RuntimeActionSheet，在澄明下呈现经典的悬浮卡片式双层操作表（上层动作集合 + 下层独立取消卡片，发丝分割线与标红破坏性操作）；
设置子页面（外观、模型、工具中心、CC-Switch、代理配置）与工作区文件浏览器全面落地 1 物理像素内缩发丝分割线（SettingsDivider / 文件项内缩 64dp、上一级目录内缩 46dp），表单单选废除 Material 单选钮并引入右侧系统高饱和蓝 Checkmark 对勾。
终端按键栏（TerminalScreen ExtraKey）：在澄明下呈现类似 iOS 终端（Blink Shell / iSH）经典键盘附件栏键帽质感——6dp 饱满圆角、0.5dp 发丝微边框、iOS 系统无衬线字体（SansSerif 10sp SemiBold 0 字距），未激活时采用柔和微透键帽，点亮（CTRL / ALT）时呈现高光系统蓝底与反白纯白字。

模拟器使用无凭证的界面测试数据；未初始化沙箱/空工程状态与用户真机状态不同，
截图只用于页面布局核验，不代表 Linux 或模型链路测试。
全项目 32 模块 architectureCheck、尺寸棘轮基线与 Robolectric/单元测试已全部通过。

## 2026-10-12 字体与图片尺度精进（消除整体偏大）

针对真机与液态玻璃主题下“字体和图片整体偏大”的问题进行系统收敛与 HIG 对齐：
1. **排版字号体系收敛**（[`Type.kt`](file:///feature/theme/src/main/java/top/wkbin/taixu/ui/theme/Type.kt)）：
   - 根因：原 `LiquidGlassTypography` 机械照搬英文 SF Pro 规格（英文字体具有极紧凑的负字距且字身矮），并将 `bodyMedium` 映射为了 16sp 的 Callout，`labelLarge` 映射为了 16sp 的 Button。在 Android 环境（使用思源黑体/厂商汉字无负字距、字身撑满 em-box）下导致全 App 的正文、聊天气泡、列表项副标题、卡片说明及按钮字号剧烈膨胀，信息密度断崖式下跌。
   - 修复：对齐 Apple HIG 比例并适配 Android 中文密度：
     - `Callout` 与 `bodyMedium`：从 16sp 收敛至 **14sp**（lineHeight 20sp），正文字号瞬间恢复精致紧凑；
     - `Button` 与 `labelLarge`：从 16sp 收敛至 **14sp SemiBold**（lineHeight 19sp），去除按钮傻大粗感；
     - `Body` 与 `bodyLarge`：从 17sp 收敛至 **15sp**（lineHeight 21sp）；
     - `Headline` 与 `titleMedium`：从 17sp 收敛至 **15sp SemiBold**（lineHeight 20sp）；
     - `Footnote` 与 `bodySmall`：从 13sp 收敛至 **12sp**（lineHeight 16sp）；
     - `LargeTitle`：从 28sp 优化至 **26sp SemiBold**（lineHeight 32sp）。
2. **多媒体与聊天图片尺寸收敛**：
   - **Markdown 渲染图片**（[`MarkdownText.kt`](file:///feature/components/src/main/java/top/wkbin/taixu/ui/components/MarkdownText.kt)）：澄明下从全宽 260dp/340dp 收敛为类似 iMessage 的精巧尺寸 **190dp 宽 / 240dp 高**，圆角精简为 10dp，并添加 0.5dp 半透明发丝微描边；
   - **聊天图片缩略图**（[`ChatMessageBubbles.kt`](file:///feature/chat/src/main/java/top/wkbin/taixu/ui/chat/ChatMessageBubbles.kt)）：澄明下从 130dp 收敛为 **104dp**，圆角 10dp 与 0.5dp 发丝微描边，去除笨拙大图感；
   - **列表与状态磁贴图标**（[`RuntimeIconTile.kt`](file:///feature/components/src/main/java/top/wkbin/taixu/ui/components/RuntimeIconTile.kt)）：澄明下将默认及大尺寸磁贴（>= 34dp）收敛至 iOS 设置标准 **30dp**（圆角 7dp，图标 16dp），杜绝 42dp 实色高饱和色块对列表行高的恶性膨胀。


