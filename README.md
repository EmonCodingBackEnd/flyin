# flyin
创建时间：20201022；项目中文名：飞入；目标：对现有成熟框架选型整合，打造自己的快速项目开发基础库。古诗云：旧时王谢堂前燕，飞入寻常百姓家。

# Git-Commit-Log 注释规范（Angular规范）
```markdown
<type>(<scope>):<subject>
// 空出一行
<body>
<footer>
```
## 说明如下：

type：必需，说明comit的类别，只允许使用下面9个标识
- feat:新功能(feature)【常用】
- fix:修补bug【常用】
- wipe:移除文件、文档或者代码
- perf:性能优化
- docs:文档(documentation)
- style:格式(不影响代码运行的变动)
- refactor:重构(既不是新增功能，也不是修改bug的代码变动)
- test:增加测试
- chore:构建过程或者辅助工具的变动
- revert:特殊情况，对当前commit用于撤销以前的commit，后面加上被撤销的commit
- version:项目版本号迭代

scope:可选，说明commit影响的范围，比如数据层、控制层、视图层等，视项目情况而不同

subject:必需，是commit目的的简短描述，一般不超过50个字符，结尾不加句号

body:可选，如果有需要，可以对本次提交更加详细的说明

footer:该部分仅用于两种情况
- 不兼容变动
```markdown
如果当前代码与上一个版本不兼容，则footer部分以`BREAKING CHANGE`开头，后面是对变动的描述、变动理由和迁移方法。
```
- 关闭Issue
```markdown
如果当前commit针对某个issue，那么可以在footer部分关闭这个issue.
```
```markdown
关闭 #123456
```

