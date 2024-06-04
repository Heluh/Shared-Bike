<template>
  <cli-layout cur-index="3">
    <div style="display: flex">
      <!--地图展示区域-->
      <!--展示围栏，对应区域内的共享单车标记，以及当前位置标记，拖动标记后加载对应位置附近的单车-->
      <el-card class="card-map">
        <div id="map-container">
        </div>
      </el-card>

      <!--功能选择区域-->
      <el-card class="right-table">
        <div style="display: flex">
          <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="围栏展示" name="first">

              <el-table :data="currentFenceTableData" stripe ref="table"
                        :header-row-style="{'font-weight': 'bold', color: '#2a5ad7'}"
                        max-height="520px">
                <el-table-column prop="fence_name" label="围栏名称" width="120px"/>
                <el-table-column prop="capacity" label="围栏容量/辆" width="120px"/>
                <el-table-column prop="area" label="所在地" width="100px"/>

                <el-table-column prop="fence_points" label="操作" width="100px">
                  <template slot-scope="scope">
                    <div class="button-row">
                      <el-button round type="primary" class="table-button"
                                 icon="el-icon-position"
                                 @click="moveToFence(scope.row)">定位
                      </el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
              <div style="display: flex; justify-content: center; margin-top: 10px">
                <el-pagination
                  @size-change="handleSizeChange_fence"
                  @current-change="handleCurrentChange_fence"
                  :current-page="currentPage"
                  :page-sizes="[5, 10, 20, 30, 50, 100]"
                  :page-size="pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="listTotal">
                </el-pagination>
              </div>

            </el-tab-pane>
            <el-tab-pane label="用户轨迹" name="second">
              <el-table :data="currentRecordTableData" stripe ref="table"
                        :header-row-style="{'font-weight': 'bold', color: '#2a5ad7'}"
                        max-height="520px">
                <el-table-column prop="userAccount" label="用户名" width="120px"/>
                <el-table-column prop="distance" label="运动距离" width="100px"/>
                <el-table-column prop="time" label="轨迹日期" width="120px"/>

                <el-table-column prop="id" label="操作" width="100px">
                  <template slot-scope="scope">
                    <div class="button-row">
                      <el-button round type="primary" class="table-button"
                                 icon="el-icon-view"
                                 @click="showTrack(scope.row)">显示
                      </el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
              <div style="display: flex; justify-content: center; margin-top: 10px">
                <el-pagination
                  @size-change="handleSizeChange_record"
                  @current-change="handleCurrentChange_record"
                  :current-page="currentRecordPage"
                  :page-sizes="[5, 10, 20, 30, 50, 100]"
                  :page-size="recordPageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="totalRecord">
                </el-pagination>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>


      </el-card>
    </div>
  </cli-layout>
</template>

<script>

import CliLayout from "~/components/base/CliLayout";
import axios from 'axios';
import Cookies from "js-cookie";

export default {
  mounted() {
    // 创建地图 并且 设置定位
    this.initMap();
    this.getFenceInfo();
    this.getRecord();
  },

  name: "detail_display",
  components: {CliLayout},
  data: () => ({
    map: null,
    pointArr: [], // 规划出来的所有点
    PolylineArr: [], // 地图上的规划实例


    fenceList: [], // 存储所有的围栏信息
    currentFenceTableData: [], // 存储当前展示的围栏信息
    currentPage: 1,                 // 当前页
    pageSize: 5,                    // 每页显示条数
    listTotal: 0,                   // 列表总数


    bikeList: [], // 单车信息
    activeName: 'first',


    recordList: [], // 轨迹信息
    currentRecordTableData: [], // 存储当前展示的轨迹信息
    currentRecordPage: 1,                 // 当前页
    recordPageSize: 5,                    // 每页显示条数
    totalRecord: 0,                   // 列表总数


  }),

  methods: {
    initMap() {
      var _this = this;
      // 百度地图API功能
      var map = new BMap.Map("map-container");    // 创建Map实例
      let point = new BMap.Point(116.348646, 39.95799); // 创建点坐标
      map.centerAndZoom(point, 16);  // 初始化地图,设置中心点坐标和地图级别
      map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
      //添加地图类型控件
      map.addControl(new BMap.MapTypeControl({
        mapTypes: [
          BMAP_NORMAL_MAP,
          BMAP_HYBRID_MAP
        ]
      }));
      _this.map = map;
    },
    /**
     * @description:  获取围栏区域信息,包括围栏点坐标以及围栏名称等
     */
    getFenceInfo() {
      const _this = this;
      axios.get('http://localhost:8888/InfoShow-service/fence/selectAllFence',
        {
          headers: {
            token: Cookies.get('token')
          },
        }).then(function (response) {
        if (response.data.code === 1) {
          _this.fenceList = response.data.fenceList;
          _this.listTotal = _this.fenceList.length;
          _this.currentFenceTableData = _this.fenceList.slice(0, _this.pageSize);
          _this.loadFence();
          //  弹窗提示
          _this.$message({
            message: '围栏信息加载成功',
            type: 'success',
            duration: 1000
          });
        } else {
          _this.$message({
            message: '围栏信息加载失败',
            type: 'error',
            duration: 1000
          });
        }
      }).catch(function (error) {
        console.log(error);
        if (error.response && error.response.status === 404) {
          _this.$message({
            message: '找不到路由',
            type: 'error',
            duration: 1000
          });
        } else if (error.response && error.response.status === 401) {
          _this.$router.push({path: '/login'});
        } else if (error.response && error.response.status === 500) {
          _this.$message({
            message: '服务器出错',
            type: 'error',
            duration: 1000
          });
        }
      });
    },

    /**
     * 获取轨迹信息
     */
    getRecord() {
      const _this = this;
      axios.get('http://localhost:8888/InfoShow-service/record/getAllRecord',
        {
          headers: {
            token: Cookies.get('token')
          },
        }).then(function (response) {
        if (response.data.code === 1) {
          _this.recordList = response.data.recordList;
          _this.totalRecord = _this.recordList.length;
          _this.currentRecordTableData = _this.recordList.slice(0, _this.pageSize);
          //  弹窗提示
          _this.$message({
            message: '轨迹信息加载成功',
            type: 'success',
            duration: 1000
          });
        } else {
          _this.$message({
            message: '轨迹信息加载失败',
            type: 'error',
            duration: 1000
          });
        }
      }).catch(function (error) {
        console.log(error);
        if (error.response && error.response.status === 404) {
          _this.$message({
            message: '路由错误',
            type: 'error',
            duration: 1000
          });
        } else if (error.response && error.response.status === 401) {
          _this.$router.push({path: '/login'});
        } else if (error.response && error.response.status === 500) {
          _this.$message({
            message: '服务器出错',
            type: 'error',
            duration: 1000
          });
        }
      });
    },

    /**
     *     将围栏信息加载到地图上
     */
    loadFence() {
      let _this = this;
      //  根据fenceList中的fence_points属性，在地图上画出多边形
      for (let i = 0; i < _this.fenceList.length; i++) {
        let fence_points = JSON.parse(_this.fenceList[i].fence_points);
        let ptr = [];
        for (let j = 0; j < fence_points.length; j++) {
          ptr.push(new BMap.Point(fence_points[j].lng, fence_points[j].lat));
        }
        let color;
        if (_this.fenceList[i].type === 1) {
          color = "#F56C6C";
        } else if (_this.fenceList[i].type === 2) {
          color = "#409EFF";
        }
        // 通过围栏坐标数组创建围栏
        let polygon = new BMap.Polygon(ptr, {strokeColor: color, strokeWeight: 3, strokeOpacity: 1});  //创建多边形
        // 多边形内部透明
        polygon.setFillColor("transparent");
        this.map.addOverlay(polygon);   //增加多边形

        let id = _this.fenceList[i].id;
        let fence_name = _this.fenceList[i].fence_name;
        let current_num = _this.fenceList[i].current_num;
        let capacity = _this.fenceList[i].capacity;
        let area = _this.fenceList[i].area;
        let create_time = _this.fenceList[i].create_date;
        let point = new BMap.Point(fence_points[0].lng, fence_points[0].lat);
        // 为每个围栏添加鼠标悬浮事件,当鼠标悬浮在围栏上时，显示信息窗口,包括围栏名称、当前围栏内的共享单车数量、围栏容量、所在地区、创建时间
        polygon.addEventListener("mouseover", function () {
          console.log("mouseover");
          // 信息窗口展示成表格的形式
          let content = `<div class="contain-table" style="color:#fff;font-size:16px;">
            <div style="margin-top:5px;height: 2px; width:100%;background-color: #1981E1 !important;"></div>
            <div style="margin-top:10px;width:220px">  围栏名称：${fence_name}</div>
            <div style="margin-top:10px;width:220px">  单车数量：${current_num}</div>
            <div style="margin-top:10px;width:220px">  围栏容量：${capacity}</div>
            <div style="margin-top:10px;width:220px">  所在地区：${area}</div>
            <div style="margin-top:10px;width:220px">  创建时间：${create_time}</div>
            </div>`;


          let opts = {
            width: 200,     // 信息窗口宽度
            height: 200,     // 信息窗口高度
            title: "围栏信息", // 信息窗口标题
            enableMessage: true//设置允许信息窗发送短息
          };

          let infoWindow = new BMap.InfoWindow(content, opts);

          // let infoWindow = new BMap.InfoWindow(content, opts);  // 创建信息窗口对象
          _this.map.openInfoWindow(infoWindow, point); //开启信息窗口
        });
        // 为每个围栏添加鼠标移出事件，当鼠标移出围栏时，关闭信息窗口
        polygon.addEventListener("mouseout", function () {
          _this.map.closeInfoWindow();
        });

        // 为每个围栏添加鼠标点击事件，当鼠标点击围栏时，显示围栏内的单车信息
        polygon.addEventListener("click", function () {
          _this.getBikes(id);
        });
      }
    },


    /**
     * 地图移动到指定的经纬度
     */
    moveToFence(data) {
      let fence_points = JSON.parse(data.fence_points);
      let point = new BMap.Point(fence_points[0].lng, fence_points[0].lat);
      console.log(point);
      this.map.centerAndZoom(point, 16);
    },

    /**
     * 修改表格每页显示数量-围栏
     */
    handleSizeChange_fence(val) {
      this.pageSize = val;
      this.currentFenceTableData = this.fenceList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },

    /**
     * 修改表格当前页-围栏
     */
    handleCurrentChange_fence(val) {
      console.log(val)
      this.currentPage = val;
      this.currentFenceTableData = this.fenceList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },

    /**
     * 修改表格每页显示数量-轨迹
     */
    handleSizeChange_record(val) {
      this.recordPageSize = val;
      this.currentRecordTableData = this.recordList.slice((this.currentRecordPage - 1) * this.recordPageSize, this.currentRecordPage * this.recordPageSize);
    },

    /**
     * 修改表格当前页-轨迹
     */
    handleCurrentChange_record(val) {
      this.currentRecordPage = val;
      this.currentRecordTableData = this.recordList.slice((this.currentRecordPage - 1) * this.recordPageSize, this.currentRecordPage * this.recordPageSize);
    },

    /**
     * 显示当前选择的轨迹
     */
    showTrack(data) {
      // 将 data.latitude 和 data.longitude 由字符串转换为数组
      let lng = data.longitude.split(",");
      let lat = data.latitude.split(",");
      let pointArr = []; // 用于存放轨迹点的数组
      let PI = 3.14159265358979324 * 3000.0 / 180.0;
      for (let i = 0; i < lat.length; i++) {
        let x = lng[i];
        let y = lat[i];
        let z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        let theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        let bd_lng = z * Math.cos(theta) + 0.0065;
        let bd_lat = z * Math.sin(theta) + 0.006;
        pointArr.push(new BMap.Point(bd_lng, bd_lat));
      }

      // 画出轨迹,并将地图中心移动到轨迹的起点.同时注意,后续需要根据某一属性来取消轨迹的显示
      let polyline = new BMap.Polyline(pointArr, {strokeColor: "#67C23A", strokeWeight: 3, strokeOpacity: 1});   //创建折线
      this.map.addOverlay(polyline);   //增加折线
      this.map.centerAndZoom(pointArr[0], 16);
    },


    // 获取单车信息
    getBikes(fenceID) {
      const _this = this;
      axios.get('http://localhost:8888/InfoShow-service/bike/selectBikeByFenceId', {
        params: {
          fenceId: fenceID
        },
        headers: {
          token: Cookies.get('token')
        },
      }).then(function (response) {
        if (response.data.code === 1) {
          _this.bikeList = response.data.bikeList;
          _this.loadBikes();
          //  弹窗提示
          _this.$message({
            message: '单车信息加载成功',
            type: 'success',
            duration: 1000
          });
        } else {
          _this.$message({
            message: '单车信息加载失败',
            type: 'error',
            duration: 1000
          });
        }
      }).catch(function (error) {
        console.log(error);
        if (error.response && error.response.status === 404) {
          _this.$message({
            message: '单车信息加载失败',
            type: 'error',
            duration: 1000
          });
        } else if (error.response && error.response.status === 401) {
          console.log("token过期");
          _this.$router.push({path: '/login'});
        } else if (error.response && error.response.status === 500) {
          _this.$message({
            message: '服务器错误',
            type: 'error',
            duration: 1000
          });
        }
      });
    },

    // 将单车信息加载到地图上
    loadBikes() {
      const _this = this;
      // 清除已经生成的mark
      _this.removeBikeMarker();

      // 遍历biekList，将根据单车经纬度生成标记点
      for (let i = 0; i < _this.bikeList.length; i++) {
        let bike = _this.bikeList[i];
        let point = new BMap.Point(bike.longitude, bike.latitude);
        let icon = new BMap.Icon(require("static/img/image.png"), new BMap.Size(30, 30), {
          imageSize: new BMap.Size(30, 30)
        });
        let marker = new BMap.Marker(point, {icon: icon});
        // let marker = new BMap.Marker(point);
        marker.id = bike.id;
        _this.map.addOverlay(marker);
      }
    },

    // 移除地图上的单车标记
    removeBikeMarker() {
      const _this = this;
      let allOverlay = _this.map.getOverlays();
      for (let i = 0; i < allOverlay.length; i++) {
        if (allOverlay[i].id) {
          _this.map.removeOverlay(allOverlay[i]);
        }
      }
    },
  }
}

</script>

<style scoped>

.map {
  width: 100%;
  height: 600px;
}

.table-button {
  padding: 5px;
  width: 80px;
}

.right-table {
  margin-left: 15px;
  width: 500px;
  height: 640px;
  position: relative;
}

#map-container {
  width: 100%;
  height: 600px;
}

.card-map {
  flex: 1;
}

.anchorBL a {
  display: none;
}

.anchorBL img {
  display: none;
}

.anchorBL span {
  display: none !important;
}

.contain-table {
  width: 100%;
  height: 100%;
  border-collapse: collapse;
}

::v-deep .BMap_bubble_title {
  color: #fff;
  font-size: 18px;
  /*font-weight: bold;*/
  text-align: left;
  background: transparent !important;
}

::v-deep [src="http://api.map.baidu.com/images/iw3.png"] {
  content: url('static/img/iw3.png');
}

::v-deep [src="http://api.map.baidu.com/images/iw3.png"] {
  /*opacity: 0.7;*/
  margin-top: -693px !important;
  filter: alpha(opacity=70);
  content: url('static/img/iw3.png');
}

::v-deep .BMap_pop .BMap_top {
  background: #3F4358 !important;
  border: 0 !important;
}

::v-deep .BMap_pop .BMap_center {
  width: 251px !important;
  border: 0 !important;
  background: #3F4358 !important;
}

::v-deep .BMap_pop .BMap_bottom {
  border: 0 !important;
  background: #3F4358 !important;
}

::v-deep .BMap_pop div:nth-child(3) {
  background: transparent !important;
}

::v-deep .BMap_pop div:nth-child(3) div {
  border-radius: 7px;
  background: #3F4358 !important;
  border: 0 !important;
}

::v-deep .BMap_pop div:nth-child(1) {
  border-radius: 7px 0 0 0;
  background: transparent !important;
  border: 0 !important;
}

::v-deep .BMap_pop div:nth-child(1) div {
  background: #3F4358 !important;
}

::v-deep .BMap_pop div:nth-child(5) {
  border-radius: 0 0 0 7px;
  background: transparent !important;
  border: 0 !important;
}

::v-deep .BMap_pop div:nth-child(5) div {
  border-radius: 7px;
  background: #3F4358 !important;
}

::v-deep .BMap_pop div:nth-child(7) {
  background: transparent !important;
}

::v-deep .BMap_pop div:nth-child(7) div {
  border-radius: 7px;
  background: #3F4358 !important;
}

::v-deep .BMap_pop div:nth-child(8) div {
  /*border-radius:7px;*/
  background: #3F4358 !important;
}

/*窗体阴影*/
::v-deep .BMap_shadow div:nth-child(5) img {
  margin-left: -1100px !important;
}

::v-deep .BMap_shadow div:nth-child(4) {
  width: 262px !important;

}

::v-deep .el-tabs__item:hover {
  color: #000;
}

::v-deep .el-tabs__item.is-active {
  color: #fff;
  background-color: #409eff;
}
</style>
