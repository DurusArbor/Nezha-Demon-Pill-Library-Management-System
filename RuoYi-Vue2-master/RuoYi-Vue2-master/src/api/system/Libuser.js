import request from '@/utils/request'



// 查询图书读者列表
export function listUser(query) {
  return request({
    url: '/library/Libuser/list',
    method: 'get',
    params: query
  })
}

// 查询图书读者详细
export function getUser(userId) {
  return request({
    url: '/library/Libuser/' + userId,
    method: 'get'
  })
}

// 新增图书读者
export function addUser(data) {
  return request({
    url: '/library/Libuser',
    method: 'post',
    data: data
  })
}

// 修改图书读者
export function updateUser(data) {
  return request({
    url: '/library/Libuser',
    method: 'put',
    data: data
  })
}

// 删除图书读者
export function delUser(userId) {
  return request({
    url: '/library/Libuser/' + userId,
    method: 'delete'
  })
}
