# 澄明符号来源

基础符号直接使用 compose-cupertino 依赖。`feature/components/.../symbols` 下的 14 个扩展符号
取自 [Compose Cupertino 0.1.0-alpha04](https://github.com/alexzhirkevich/compose-cupertino/tree/0.1.0-alpha04/cupertino-icons-extended/src/commonMain/kotlin/io/github/alexzhirkevich/cupertino/icons)。
仅调整 Kotlin package；保留原作者版权与 Apache 2.0 文件头。

按实际使用范围纳入源码，避免完整扩展库中数千个未使用符号拖累 Android Debug DEX 构建。
符号选择统一由 ChengmingSymbols 管理，业务页面继续使用 RuntimeIcon。
