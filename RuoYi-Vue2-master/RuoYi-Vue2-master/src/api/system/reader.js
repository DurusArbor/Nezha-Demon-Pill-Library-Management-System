import request from '@/utils/request'

// 读者自助：在线书城（图书列表含可借数量）
export function readerBooks(query) {
  return request({
    url: '/reader/books',
    method: 'get',
    params: query
  })
}

// 读者自助：我的借阅列表
export function myBorrows() {
  return request({
    url: '/reader/myBorrows',
    method: 'get'
  })
}

// 读者自助：在线借书
export function readerBorrow(bookId) {
  return request({
    url: '/reader/borrow',
    method: 'post',
    data: { bookId }
  })
}

// 读者自助：在线还书
export function readerReturn(borrowId) {
  return request({
    url: '/reader/return/' + borrowId,
    method: 'post'
  })
}

// 读者自助：续借
export function readerRenew(borrowId) {
  return request({
    url: '/reader/renew/' + borrowId,
    method: 'post'
  })
}

// 读者自助：我的预约列表
export function myReserves() {
  return request({
    url: '/reader/myReserves',
    method: 'get'
  })
}

// 读者自助：预约图书
export function readerReserve(bookId) {
  return request({
    url: '/reader/reserve',
    method: 'post',
    data: { bookId }
  })
}

// 读者自助：取消预约
export function readerCancelReserve(reserveId) {
  return request({
    url: '/reader/reserve/' + reserveId,
    method: 'delete'
  })
}

// 读者自助：我的罚金列表
export function myFines() {
  return request({
    url: '/reader/myFines',
    method: 'get'
  })
}

// 读者自助：图书分类
export function readerCategories() {
  return request({
    url: '/reader/categories',
    method: 'get'
  })
}

// 读者自助：缴纳本人罚金
export function readerPayFine(fineId) {
  return request({
    url: '/reader/fine/pay/' + fineId,
    method: 'post'
  })
}

// 读者自助：图书馆公告（分页，仅正常状态）
export function readerNotices(query) {
  return request({
    url: '/reader/notices',
    method: 'get',
    params: query
  })
}

// 读者自助：我的修行（境界、魔丸/灵珠、功德魔性、进度）
export function getCultivation() {
  return request({
    url: '/reader/cultivation',
    method: 'get'
  })
}

// 读者自助：每日签到
export function readerSign() {
  return request({
    url: '/reader/sign',
    method: 'post'
  })
}

// 读者自助：每日奇遇/天劫（每天最多一次，20% 概率触发）
export function readerFortune() {
  return request({
    url: '/reader/fortune',
    method: 'post'
  })
}

// 读者自助：魔性净化仪式（消耗 100 功德净化 10 点魔性）
export function readerPurify() {
  return request({
    url: '/reader/purify',
    method: 'post'
  })
}

// 读者自助：开仙丹盲盒（20 功德/次，每日最多 3 次，魔丸不可抽）
export function readerLottery() {
  return request({
    url: '/reader/lottery',
    method: 'post'
  })
}

// 读者自助：兑换免罚令（50 功德/张，缴罚金时自动抵消一次）
export function readerBuyToken() {
  return request({
    url: '/reader/token/buy',
    method: 'post'
  })
}

// 读者自助：封神榜（功德排行榜）
export function getRank() {
  return request({
    url: '/reader/rank',
    method: 'get'
  })
}

