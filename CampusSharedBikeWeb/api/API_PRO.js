export default {

  color: {
    primaryColor: '#2a5ad7',
    secondaryColor: '#628cf6',
  },

  baseURL: 'http://127.0.0.1:8888',
  method: 'post',

  /**
   * 围栏相关
   */
  getFenceList: {url: 'fence-service/fence/list', method: 'get'},     // 获取围栏列表
  addNewFence: {url: 'fence-service/fence/save', method: 'post'},     // 添加新围栏
  updateExistFence: {url: 'fence-service/fence/update', method: 'put'},     // 更新已有围栏
  deleteExistFence: {url: 'fence-service/fence/delete', method: 'delete'},     // 删除已有围栏
  checkIfPosInAnyFence: {url: 'fence-service/fence/check', method: 'get'},     // 检查坐标是否在任意围栏内
  getFenceGraphData: {url: 'fence-service/fence/graph-data', method: 'get'}, // 获取围栏图表数据

  /**
   * 捐赠活动相关
   */
  getAllCharityEventList: {url: 'charity-service/charity/activity/all-list', method: 'get'},     // 获取捐赠列表
  getOpenCharityEventList: {url: 'charity-service/charity/activity/open-list', method: 'get'},     // 获取当前开放的捐赠列表
  getOneCharityEvent: { url: 'charity-service/charity/activity/one', method: 'get' }, // 获取捐赠活动详情
  deleteCharityEvent: { url: 'charity-service/charity/activity/delete', method: 'delete' }, // 删除捐赠活动
  addCharityEvent: { url: 'charity-service/charity/activity/save', method: 'post' }, // 新增捐赠活动
  updateCharityEvent: { url: 'charity-service/charity/activity/update', method: 'put' }, // 更新捐赠活动


  /**
   * 捐赠车辆相关
   */
  getBindCharityBikeList: {url: 'charity-service/charity/bike/bind-list', method: 'get'},     // 获取捐赠车辆列表
  getInCharityBikeList: { url: 'charity-service/charity/bike/in-list', method: 'get' },            // 获取捐赠车辆详情
  addCharityBike: { url: 'charity-service/charity/bike/add', method: 'post' }, // 新增捐赠车辆
  bindCharityBike: { url: 'charity-service/charity/bike/bind', method: 'post' }, // 绑定捐赠车辆

  requestLogin: {url: 'login-service/account/login', method: 'post'}, // 登录
}
