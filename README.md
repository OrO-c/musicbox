# 通用语音盒模板 (Universal Voice Box Template)

一个基于Kotlin和Jetpack Compose的Android应用模板，其内容与行为完全由外部的index.json配置文件和音频资源决定，实现"一次开发，多处使用"。

## 项目特性

### 🎯 核心功能
- **配置驱动**: 完全通过外部JSON配置文件控制应用内容
- **多源导入**: 支持内置、本地和网络三种资源导入方式
- **自适应播放器**: 根据音频时长自动调整播放界面
- **Material 3设计**: 遵循最新的Material Design规范

### 📱 技术栈
- **架构**: MVI + Clean Architecture
- **UI**: Jetpack Compose + Material 3
- **依赖注入**: Hilt
- **异步处理**: Kotlin Coroutines + Flow
- **音频播放**: AndroidX Media3 (ExoPlayer)
- **网络请求**: Retrofit + OkHttp
- **本地存储**: DataStore

## 项目结构

```
UniversalVoiceBox/
├── app/                    # 应用模块
│   ├── src/main/
│   │   ├── assets/         # 内置资源包
│   │   ├── java/           # 应用代码
│   │   └── res/            # 资源文件
│   └── build.gradle.kts
├── data/                   # 数据层
│   ├── src/main/java/
│   │   ├── local/          # 本地存储
│   │   ├── network/        # 网络服务
│   │   ├── repository/     # 仓库实现
│   │   └── di/             # 依赖注入
│   └── build.gradle.kts
├── domain/                 # 领域层
│   ├── src/main/java/
│   │   ├── model/          # 数据模型
│   │   ├── repository/     # 仓库接口
│   │   └── usecase/        # 用例
│   └── build.gradle.kts
├── ui/                     # 界面层
│   ├── src/main/java/
│   │   ├── component/      # UI组件
│   │   ├── screen/         # 界面
│   │   ├── theme/          # 主题
│   │   └── viewmodel/      # ViewModel
│   └── build.gradle.kts
└── build.gradle.kts
```

## 快速开始

### 1. 环境要求
- Android Studio Hedgehog | 2023.1.1 或更高版本
- Android SDK 34
- Kotlin 1.9.10
- JDK 17

### 2. 克隆项目
```bash
git clone <repository-url>
cd UniversalVoiceBox
```

### 3. 构建运行
```bash
./gradlew build
```

在Android Studio中打开项目，连接设备或启动模拟器，点击运行按钮。

## 资源包格式

### 文件结构
资源包必须为ZIP格式，包含以下结构：
```
voice_pack.zip
├── index.json          # 配置文件（必需）
├── audio/              # 音频文件夹（必需）
│   ├── line01.mp3
│   ├── line02.mp3
│   └── ...
└── icons/              # 图标文件夹（可选）
    ├── icon_a.png
    └── ...
```

### JSON配置格式
```json
{
  "title": "语音盒标题",
  "sections": [
    {
      "id": "character01",
      "name": "角色A",
      "icon": "icon_a.png"
    }
  ],
  "voices": [
    {
      "id": "voice001",
      "text": "这是一句经典台词",
      "audio_file": "audio/line01.mp3",
      "section_id": "character01"
    }
  ]
}
```

### 字段说明
- `title`: 应用栏显示的标题
- `sections`: 栏目列表
  - `id`: 唯一标识符
  - `name`: 显示名称
  - `icon`: 图标文件名（可选）
- `voices`: 语音列表
  - `id`: 唯一标识符
  - `text`: 列表显示的文字
  - `audio_file`: 音频文件路径（相对于ZIP根目录）
  - `section_id`: 归属的栏目ID

## 导入方式

### 1. 内置资源
应用启动时自动加载`assets/default_voice_pack/`目录下的资源包。

### 2. 本地导入
1. 点击应用栏菜单按钮
2. 选择"从本地导入"
3. 选择ZIP格式的资源包文件
4. 等待解压和验证完成

### 3. 网络导入
1. 点击应用栏菜单按钮
2. 选择"从网络导入"
3. 输入资源包的直链URL
4. 点击"开始下载"
5. 等待下载、解压和验证完成

## 播放功能

### 短音频 (≤10秒)
- 点击列表项立即播放
- 播放期间不可中断
- 播放时背景色变为绿色
- 无控制条

### 长音频 (>10秒)
- 点击列表项后在底部显示迷你播放条
- 包含播放/暂停按钮、进度条、时长显示
- 支持拖拽进度条跳转
- 支持播放/暂停控制

## 二次开发

### 1. 修改主题
编辑`ui/src/main/java/com/example/universalvoicebox/ui/theme/`下的文件：
- `Color.kt`: 定义颜色
- `Type.kt`: 定义字体
- `Theme.kt`: 定义主题

### 2. 添加新功能
1. 在`domain`层定义数据模型和接口
2. 在`data`层实现具体逻辑
3. 在`ui`层创建界面组件
4. 在`app`层集成到主界面

### 3. 自定义播放器
修改`data/src/main/java/com/example/universalvoicebox/data/repository/AudioPlayerRepositoryImpl.kt`：
- 调整短音频时长阈值
- 修改播放器行为
- 添加新的播放控制功能

### 4. 扩展导入方式
在`data/src/main/java/com/example/universalvoicebox/data/repository/VoicePackRepositoryImpl.kt`中添加新的导入逻辑。

## 打包新资源

### 1. 准备资源
1. 创建音频文件并放入`audio/`文件夹
2. 准备图标文件（可选）
3. 创建`index.json`配置文件

### 2. 打包ZIP
```bash
zip -r voice_pack.zip index.json audio/ icons/
```

### 3. 分发方式
- **本地分享**: 直接分享ZIP文件
- **网络分享**: 上传到云存储并分享直链
- **内置打包**: 放入应用的`assets`目录

## 常见问题

### Q: 音频文件无法播放？
A: 检查音频文件格式是否支持（推荐MP3格式），确保文件路径正确。

### Q: 导入失败？
A: 检查ZIP文件是否包含必需的`index.json`文件，确保JSON格式正确。

### Q: 网络导入很慢？
A: 检查网络连接，确保URL是直链且服务器响应正常。

### Q: 如何添加新的音频格式支持？
A: 修改`AudioPlayerRepositoryImpl.kt`中的播放器配置。

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 许可证

本项目采用MIT许可证，详见[LICENSE](LICENSE)文件。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 提交Issue
- 发送邮件
- 参与讨论

---

**注意**: 这是一个模板项目，可以根据具体需求进行定制和扩展。建议在使用前仔细阅读代码注释和文档。
