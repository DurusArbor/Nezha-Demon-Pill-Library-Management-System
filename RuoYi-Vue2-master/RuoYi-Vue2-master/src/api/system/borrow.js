import request from '@/utils/request'

// 查询图书借阅记录列表
export function listBorrow(query) {
  return request({
    url: '/system/borrow/list',
    method: 'get',
    params: query
  })
}

// 查询图书借阅记录详细
export function getBorrow(borrowId) {
  return request({
    url: '/system/borrow/' + borrowId,
    method: 'get'
  })
}

// 新增图书借阅记录
export function addBorrow(data) {
  return request({
    url: '/system/borrow',
    method: 'post',
    data: data
  })
}

// 修改图书借阅记录
export function updateBorrow(data) {
  return request({
    url: '/system/borrow',
    method: 'put',
    data: data
  })
}

// 删除图书借阅记录
export function delBorrow(borrowId) {
  return request({
    url: '/system/borrow/' + borrowId,
    method: 'delete'
  })
}

// 借书业务
export function doBorrow(userId, bookId) {
  return request({
    url: '/system/borrow/doBorrow',
    method: 'post',
    params: { userId, bookId }
  })
}

// 还书业务
export function doReturn(borrowId) {
  return request({
    url: '/system/borrow/doReturn/' + borrowId,
    method: 'post'
  })
}

// 续借业务
export function renewBorrow(borrowId) {
  return request({
    url: '/system/borrow/renew/' + borrowId,
    method: 'post'
  })
}
