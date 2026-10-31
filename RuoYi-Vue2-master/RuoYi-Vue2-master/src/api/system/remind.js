import request from '@/utils/request'

// 管理端：催缴记录列表
export function listRemind(query) {
  return request({
    url: '/system/remind/list',
    method: 'get',
    params: query
  })
}

// 管理端：对某笔逾期借阅发起催缴
export function urgeRemind(borrowId) {
  return request({
    url: '/system/remind/urge/' + borrowId,
    method: 'post'
  })
}

// 读者端：我的催缴通知（默认未读，可传 status）
export function myReminds(status) {
  return request({
    url: '/reader/reminds',
    method: 'get',
    params: status !== undefined ? { status } : {}
  })
}

// 读者端：标记催缴为已读
export function readRemind(remindId) {
  return request({
    url: '/reader/reminds/read/' + remindId,
    method: 'post'
  })
}
