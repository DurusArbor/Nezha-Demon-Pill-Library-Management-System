import request from '@/utils/request'

// 图书分类列表
export function listCategory(query) {
  return request({
    url: '/system/category/list',
    method: 'get',
    params: query
  })
}

// 分类下拉（不分页，用于表单选择）
export function categoryOptions() {
  return request({
    url: '/system/category/list?pageNum=1&pageSize=100',
    method: 'get'
  })
}

export function getCategory(categoryId) {
  return request({ url: '/system/category/' + categoryId, method: 'get' })
}
export function addCategory(data) {
  return request({ url: '/system/category', method: 'post', data })
}
export function updateCategory(data) {
  return request({ url: '/system/category', method: 'put', data })
}
export function delCategory(categoryId) {
  return request({ url: '/system/category/' + categoryId, method: 'delete' })
}
