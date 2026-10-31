import request from '@/utils/request'

// 查询借阅罚金列表
export function listFine(query) {
  return request({
    url: '/system/fine/list',
    method: 'get',
    params: query
  })
}

// 确认缴纳罚金
export function payFine(fineId) {
  return request({
    url: '/system/fine/pay/' + fineId,
    method: 'post'
  })
}
