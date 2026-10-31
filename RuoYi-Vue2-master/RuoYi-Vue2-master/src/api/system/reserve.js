import request from '@/utils/request'

// 查询图书预约列表
export function listReserve(query) {
  return request({
    url: '/system/reserve/list',
    method: 'get',
    params: query
  })
}

// 查询图书预约详细
export function getReserve(reserveId) {
  return request({
    url: '/system/reserve/' + reserveId,
    method: 'get'
  })
}

// 取消预约（管理员代操作）
export function cancelReserve(reserveId) {
  return request({
    url: '/system/reserve/cancel/' + reserveId,
    method: 'post'
  })
}
