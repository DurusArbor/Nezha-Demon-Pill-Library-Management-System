# 哪吒魔丸图书管理系统 · 前端

基于 RuoYi-Vue2（Vue 2 + Element UI + Vuex + Vue Router）的图书馆管理系统前端，使用 ECharts 5 渲染数据看板。

## 启动

```bash
npm install
npm run dev
```

默认端口 `81`，开发代理指向后端 `http://localhost:8080`（见 `vue.config.js`）。

## 主要定制页面

- `src/views/index.vue`：管理端首页数据看板（借阅趋势 / 热门图书 / 逾期率 / 魔丸灵珠分布）
- `src/views/readerSelf/`：读者中心（签到、奇遇、称号、一念成仙、仙丹盲盒、免罚令、封神榜）
- `src/views/readerSelf/myFines.vue`：仿支付收银台（微信 / 支付宝）
- `src/views/reader/`、`borrow/`、`reserve/`、`fine/`：管理端各业务页（搜索增强、隐藏 ID、催缴）
- `src/views/register.vue`：注册页空值校验

完整说明（功能清单、数据库初始化、演示账号）见项目根目录 [`README.md`](../../README.md)。
