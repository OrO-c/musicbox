项目提示词：通用语音盒模板 (Universal Voice Box Template)

核心目标： 使用Kotlin开发一个高度可配置的Android语音盒应用模板。内容完全由index.json文件定义，支持三种导入方式，并根据音频时长智能切换UI。

一、核心功能

1. 配置驱动： 从index.json读取所有内容，结构包含：
   · title：应用标题。
   · sections[]：栏目数组（含id, name, icon）。
   · voices[]：语音数组（含id, text, audio_file, section_id）。
2. 三级导入机制（按优先级）：
   · A. 源码嵌入： 预置文件到assets目录。
   · B. 网络拉取： 提供向导，输入URL下载并解压zip包到本地存储。
   · C. 本地导入： 提供向导，选择本地zip文件并解压。
3. 智能音频播放：
   · 短音频（≤10s）： 点击即播放。列表项显示音浪动画反馈，无进度条。
   · 长音频（>10s）： 提供底部迷你播放条控件（播放/暂停、进度条、时长）。

二、UI/UX关键设计

· 主界面： 采用底部导航或侧边抽屉展示sections。内容区为语音列表。
· 播放UI：
  · 短音频： 列表项播放时背景色变化或伴有音浪动画。
  · 长音频： 固定底部播放条，可控制播放进度。
· 导入向导： 多步骤界面（输入URL/选择文件 -> 下载/解压进度条 -> 完成/失败结果提示）。
· 视觉： 采用Material 3设计语言，状态明确（如播放中绿色、错误红色）。

三、技术栈要求

· 语言： Kotlin
· 架构： MVVM/MVI + Jetpack Compose (优先)
· 依赖： AndroidX, Coil, Retrofit, 等
· 目标API： 最低 Android 8.0 (API 26)

输出要求： 提供完整、可编译的Android Studio项目源码及详细的README.md使用文档。
