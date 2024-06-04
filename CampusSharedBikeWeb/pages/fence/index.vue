<template>
  <cli-layout cur-index="2">
    <div style="display: flex; flex-direction: column; min-width: 1340px">
      <!-- 上方地图区 -->
      <div style="display: flex">
        <!-- 左侧地图展示 -->
        <el-card class="card-map">
          <div id="map-container"/>
        </el-card>

        <!-- 右侧围栏列表 -->
        <el-card class="right-table">
          <div style="display: flex">
            <el-button type="primary" @click="openFenceDialog">添加围栏</el-button>
          </div>

          <div style="display: flex; margin: 10px 0">
            <div style="color: #2a5ad7; font-weight: bold; margin-right: 20px;">围栏类型快选</div>
            <div style="margin-top: 2px; display: flex">
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAllType" @change="handleCheckAllTypeChange">全选
              </el-checkbox>
              <div style="margin-left: 20px;"/>
              <el-checkbox-group v-model="checkedOnOptions" @change="handleCheckedTypeChange">
                <el-checkbox v-for="onType in onTypeOptions" :label="onType" :key="onType">
                  {{ onType }}
                </el-checkbox>
              </el-checkbox-group>
            </div>
          </div>

          <el-table :data="fenceTableData" stripe ref="fenceTable"
                    :header-row-style="{'font-weight': 'bold', color: '#2a5ad7'}"
                    max-height="480px"
                    @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="30"/>
            <el-table-column prop="fence_name" label="围栏名称"/>
            <el-table-column prop="type_name" label="围栏类型"/>
            <!-- 添加颜色选择器 -->
            <el-table-column prop="color" label="围栏颜色" width="80px">
              <template slot-scope="scope">
                <el-color-picker v-model="scope.row.color" size="mini" @change=""/>
              </template>
            </el-table-column>
            <!--        <el-table-column prop="createTime" label="创建时间" />
                    <el-table-column prop="updateTime" label="更新时间" />-->
            <el-table-column prop="operation" label="操作" width="100px">
              <template slot-scope="scope">
                <div class="button-row">
                  <el-button round type="primary" class="table-button" @click="edit(scope.$index, scope.row)"
                             icon="el-icon-edit"></el-button>
                  <el-button round type="danger" class="table-button" @click="del(scope.$index, scope.row)"
                             icon="el-icon-delete"></el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <div style="display: flex; justify-content: center; margin-top: 10px">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[10, 20, 30, 50, 100]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="listTotal">
            </el-pagination>
          </div>
        </el-card>
      </div>

      <!-- 下方统计展示区 -->
      <div style="display: flex; margin-top: 15px">
        <el-card class="chartOb">
          <div style="display: flex; height: 100%; justify-content: space-around">
            <div class="chartHx">
              <div id="fenceType"/>
            </div>
            <div class="chartHx">
              <div id="fenceType2Max"/>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 围栏编辑弹窗 -->
    <el-dialog
      :title="isAddFence ? '添加围栏' : '编辑围栏'"
      :visible.sync="fenceDialogVisible"
      width="80%"
      append-to-body
      :before-close="handleClose"
      @opened="prepareNewInput">
      <div class="dialog-info-block">
        <div class="dialog-title">围栏名称</div>
        <el-input v-model="inputFenceName" placeholder="请输入围栏名称"
                  clearable style="width: 180px; margin-right: 15px"/>
        <div class="dialog-title">围栏类型</div>
        <el-select v-model="inputFenceType" placeholder="请选择围栏类型"
                   style="width: 150px; margin-right: 15px">
          <el-option label="车辆运营区" :value="1"/>
          <el-option label="指定停车区" :value="2"/>
        </el-select>
        <div v-if="inputFenceType === 2" class="dialog-title">最大停车数量</div>
        <el-input-number v-if="inputFenceType === 2"
                         v-model="inputFenceMaximumNumber"
                         controls-position="right"
                         style="width: 120px; margin-right: 15px"
                         :min="1" :max="200"></el-input-number>
        <div class="dialog-title">{{ isAddFence ? '录入模式' : '修改模式' }}</div>
        <div class="switch-container">
          <el-switch v-if="isAddFence" v-model="isUserDrawOnMap"
                     @change="changeUserMapCursor" active-color="#13ce66" inactive-color="#999"/>
          <el-switch v-else v-model="isLineNeedDrag"
                     @change="onEditDragLine" active-color="#13ce66" inactive-color="#999"/>
        </div>
        <el-button type="primary" style="position: absolute; right: 0"
                   @click="finishInputFence">完成
        </el-button>
      </div>
      <div id="dialog-map-container"></div>
    </el-dialog>
  </cli-layout>

</template>

<script>
import CliLayout from "~/components/base/CliLayout.vue";
import API_PRO from "~/api/API_PRO";
import API from "~/api";
import AreaGraph from "~/components/fence/AreaGraph.vue";
import Cookies from "js-cookie";

export default {
  name: "fence-index",
  components: {AreaGraph, CliLayout},
  data() {
    return {
      // 外地图展示
      map: null,                      // 普通外展示地图实例
      fenceTableData: [],             // 围栏列表数据
      displayPolylineList: [],        // 外展示地图上的围栏实例
      needDisplayFence: [],           // 需要显示的围栏数据
      currentPage: 1,                 // 当前页
      pageSize: 10,                    // 每页显示条数
      listTotal: 0,                   // 列表总数
      onTypeOptions: ['车辆运营区', '指定停车区'],  // 快速选择展示围栏类型
      checkAllType: false,            // 是否全选围栏类型
      checkedOnOptions: [],           // 选中的围栏类型
      isIndeterminate: false,         // 是否半选围栏类型

      // 添加/编辑围栏展示
      dialogMap: null,                // 弹窗编辑地图实例
      editPolyline: {},               // 编辑地图上的实例信息
      userDrawPointList: [],          // 用户添加围栏时暂存点列表
      isUserDrawOnMap: false,         // 是否用户添加围栏线
      isUserDragLineOnMap: {          // 是否用户拖拽围栏折线
        index: null,
        off: false
      },
      nowUserEditFence: {             // 当前用户编辑的围栏信息
        index: null,
        name: "",
      },
      fenceDialogVisible: false,      // 添加围栏弹窗
      isAddFence: false,              // 是否是添加围栏 否则是编辑围栏
      hasEditFence: false,            // 是否编辑过围栏
      isLineNeedDrag: false,          // 是否显示折线节点

      inputFenceName: "",             // 绑定弹窗内围栏名称
      inputFenceType: "",             // 绑定弹窗内围栏类型
      inputFenceMaximumNumber: 0,     // 绑定弹窗内围栏最大数量

      // 默认参数
      defaultMaximumNumber: 20,
      defaultColorList: [["#409EFF", "#67C23A", "#0000ff"],
        ["#ff0000", "#ff7300", "#ae00ff"]],    // 默认颜色列表
      nowDefaultColorIndex: [0, 0],      // 当前默认颜色索引

      primaryColor: API_PRO.color.primaryColor,     // 主题色

      graph1: null,                   // 图表1
      graph2: null,                   // 图表2

      token: Cookies.get('token'),
    };
  },

  mounted() {
    console.log("token", this.token)
    console.log(Cookies.get('token'))
    this.createBaiduMap('map-container');          // 初始化地图
    this.loadFenceList();                     // 加载围栏列表
    this.loadFenceGraphData();                // 加载围栏图形数据
  },

  methods: {
    /**
     * 加载围栏统计数据
     */
    loadFenceGraphData() {
      let data = {
        token: this.token
      };

      API.getFenceGraphData(data).then(res => {
        if (res.code) {
          this.$message.error(res.msg);
          return;
        }

        this.initGraph("fenceType", res[0]);
        this.initGraph("fenceType2Max", res[1]);

      }).catch(err => {
        console.log(err);
        this.$message.error("加载围栏统计数据失败：" + err);
      })
    },

    initGraph(chartId, data) {
      let chartDom = document.getElementById(chartId);
      if (chartId === "fenceType") {
        this.graph1 = this.$echarts.init(chartDom);
      } else {
        this.graph2 = this.$echarts.init(chartDom);
      }

      let option = {
        title: {
          text: data.titleText,
          subtext: data.titleSubtext,
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          top: 'bottom'
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: false},
            restore: {show: false},
            saveAsImage: {show: true}
          }
        },
        series: [
          {
            name: data.seriesName,
            type: 'pie',
            radius: ['25%', '55%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              fontSize: 16,
              position: 'left'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
                fontWeight: 'bold',
                position: 'center'
              }
            },
            /*labelLine: {
              show: false
            },*/
            data: data.seriesData
          }
        ]
      };

      if (chartId === "fenceType") {
        this.graph1.setOption(option);
      } else {
        this.graph2.setOption(option);
      }
    },

    /**
     * 快选checkbox全选框响应
     * @param val
     */
    handleCheckAllTypeChange(val) {
      this.checkedOnOptions = val ? this.onTypeOptions : [];
      this.isIndeterminate = false;

      if (val) {    // 全选
        this.selectAllRowsInTable();
      } else {
        this.selectNoRowsInTable();
      }
    },

    /**
     * 快选checkbox组选中的值变化
     * @param value
     */
    handleCheckedTypeChange(value) {
      let checkedCount = value.length;
      this.checkAllType = checkedCount === this.onTypeOptions.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.onTypeOptions.length;

      if (this.checkAllType) {
        this.selectAllRowsInTable();
      } else if (this.isIndeterminate) {
        this.selectSpecificRowsInTable(value);
      } else {
        this.selectNoRowsInTable();
      }
    },

    /**
     * 选中指定类型的行
     * @param type_name 类型名称 '车辆运营区' | '指定停车区'
     */
    selectSpecificRowsInTable(type_name) {
      let type = type_name[0] === '车辆运营区' ? 1 : 2;
      let currentTypeRows = [];

      // 清除所有已选中的行
      this.$refs.fenceTable.clearSelection();
      this.fenceTableData.forEach((item, index) => {
        if (item.type === type) {
          this.$refs.fenceTable.toggleRowSelection(item, true);
          currentTypeRows.push(item);
        }
      });

      // 处理选中的行画画 此处会hook自动调用handleSelectionChange
    },

    /**
     * 清除所有选中的行
     */
    selectNoRowsInTable() {
      // 清空所有选中的行
      this.$refs.fenceTable.clearSelection();

      // 处理选中的行画画 此处会hook自动调用handleSelectionChange
    },

    /**
     * 选中所有行
     */
    selectAllRowsInTable() {
      // 清除所有已选中的行
      this.$refs.fenceTable.clearSelection();
      // 选中所有的行
      this.$refs.fenceTable.toggleAllSelection();

      // 处理选中的行画画 此处会hook自动调用handleSelectionChange
    },

    /**
     * 添加围栏dialog前的准备工作
     */
    prepareNewInput() {
      if (this.isAddFence) {
        this.createBaiduMap('dialog-map-container');
        this.addTempFence();
      } else {
        this.createBaiduMap('dialog-map-container');
        this.editTempFence();
        this.onEditDragLine();
      }
    },

    /**
     * 修改表格每页显示数量时
     */
    handleSizeChange(val) {
      this.pageSize = val;
      this.loadFenceList();
    },

    /**
     * 修改表格当前页时
     */
    handleCurrentChange(val) {
      this.currentPage = val;
      this.loadFenceList();
    },

    /**
     * 加载围栏列表
     */
    loadFenceList() {
      let data = {
        page: this.currentPage,
        size: this.pageSize,
        token: this.token
      };

      API.getFenceList(data).then(res => {
        if (res.code) {
          this.$message.error("加载围栏列表失败：" + res.msg);
          return;
        }

        res.list.forEach(item => {
          item.fence_points = JSON.parse(item.fence_points);    // 转换为对象
          item.type_name = item.type === 1 ? "车辆运营区" : "指定停车区(" + item.capacity + ")";   // 围栏类型名称
          item.color = this.generateDefaultColor(item.type);
          let pts = [];
          item.fence_points.forEach((v, i) => {
            pts.push(new BMap.Point(v.lng, v.lat));
          });
          item.fence_points = pts;
        });

        this.fenceTableData = res.list;
        this.listTotal = res.count;
        this.checkAllType = true;
        this.handleCheckAllTypeChange(true);
      }).catch(err => {
        console.log(err);
        this.$message.error("加载围栏列表失败：" + err);
      });
    },

    /**
     * 创建对应的百度地图对象
     * @param id 地图容器id
     */
    createBaiduMap(id) {
      let _this = this;
      let map = new BMap.Map(id, {enableMapClick: false});
      let point = new BMap.Point(116.348646, 39.95799);
      map.centerAndZoom(point, 16);         // 初始化地图中心与缩放级别
      map.enableScrollWheelZoom(true);
      // 添加地图类型控件
      map.addControl(new BMap.MapTypeControl({
        mapTypes: [
          BMAP_NORMAL_MAP,
          BMAP_HYBRID_MAP
        ]
      }));
      // 单击获取点击的经纬度
      map.addEventListener("click", function (e) {
        if (_this.fenceTableData.length) {
          if (_this.isUserDrawOnMap) {
            let {index} = _this.nowUserEditFence;
            _this.fenceTableData[index].points.push(e.point);
            _this.editPolyline.setPath(_this.fenceTableData[index].points);

            // 绘制这些点的数据
            let pointCollection = new BMap.PointCollection(
              _this.fenceTableData[index].points,
              {
                size: "BMAP_POINT_SIZE_HUGE",
                color: "#ff0000",
              }
            );
            _this.userDrawPointList.push(pointCollection);
            pointCollection.addEventListener("click", function (e) {
            });
            map.addOverlay(pointCollection);
          }
        }
      });
      if (id === "map-container") {
        this.map = map;
      } else {
        this.dialogMap = map;
      }
    },

    /**
     * 格式化时间
     * @param date {Date}
     * @returns {string} EXP: 2020-01-01 00:00:00
     */
    formatDate(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');

      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },

    /**
     * 选择需要展示在外地图上的围栏
     * @param val
     */
    handleSelectionChange(val) {
      this.needDisplayFence = val;
      this.reDrawDisplayMapFence();
    },

    /**
     * 重新绘制地图上的围栏
     */
    reDrawDisplayMapFence() {
      this.clearMapOverlay("display");
      this.createBaiduPolyline(this.needDisplayFence, "display");
    },

    /**
     * 生成默认围栏展示颜色
     * @returns {string} EXP: #409EFF
     */
    generateDefaultColor(type) {
      let typeIndex = type - 1;
      let color = this.defaultColorList[typeIndex][this.nowDefaultColorIndex[typeIndex]];
      this.nowDefaultColorIndex[typeIndex]++;
      if (this.nowDefaultColorIndex[typeIndex] >= this.defaultColorList[typeIndex].length) {
        this.nowDefaultColorIndex[typeIndex] = 0;
      }
      return color;
    },

    /**
     * 预添加围栏 用于创建围栏实例
     */
    addTempFence() {
      this.changeUserMapCursor(false);             // 恢复鼠标原始状态
      const currentTime = new Date();
      const formattedDateTime = this.formatDate(currentTime);
      let tempFenceName = `新围栏 ${formattedDateTime}`;

      let tempFence = {
        name: tempFenceName,
        points: [],
      };
      this.fenceTableData.push(tempFence);            // 预添加围栏数据列表
      this.createBaiduPolyline([tempFence], "dialog");        // 创建围栏实例
      this.nowUserEditFence.name = tempFenceName;
      this.nowUserEditFence.index = this.fenceTableData.length - 1;
      this.inputFenceMaximumNumber = this.defaultMaximumNumber;          // 设置默认最大围栏数量为20
    },

    /**
     * 创建围栏百度实例
     * @param arr {Array} 围栏数据列表
     * @param mapType {String} 目标渲染地图类型
     */
    createBaiduPolyline(arr, mapType) {
      if (mapType === "dialog") {
        arr.forEach((item) => {
          let polyline = new BMap.Polyline(item.fence_points, {
            strokeColor: this.primaryColor,
            strokeWeight: 3,
            strokeOpacity: 1,
          }); // 创建折线
          this.dialogMap.addOverlay(polyline);
          this.editPolyline = polyline;
        });
      } else {
        arr.forEach((item) => {
          let polyline = new BMap.Polyline(item.fence_points, {
            strokeColor: item.color,
            strokeWeight: 3,
            strokeOpacity: 1,
          }); // 创建折线
          this.map.addOverlay(polyline);
          this.displayPolylineList.push(polyline);
        });
      }
    },

    /**
     * 判断浮点数是否相等
     * @param num1 {Number}
     * @param num2 {Number}
     * @returns {Number} -1: 小于 / 0: 等于 / 1: 大于
     */
    areEqualWithPrecision(num1, num2) {
      const precision = 0.000001;
      const diff = Math.abs(num1 - num2);
      return diff < precision ? 0 : num1 > num2 ? 1 : -1;
    },

    /**
     * 清除地图显示的围栏
     * @param mapType {String} 目标渲染地图类型
     */
    clearMapOverlay(mapType) {
      if (mapType === "dialog") {
        this.dialogMap.clearOverlays();
        this.editPolyline = {};
      } else {
        this.map.clearOverlays();
        this.displayPolylineList = [];
      }
    },

    // 开启关闭当规划编辑
    onEditDragLine() {
      const index = this.nowUserEditFence.index;
      this.changeUserMapCursor(false);
      this.isUserDragLineOnMap.index = index
      if (this.isLineNeedDrag) {
        this.hasEditFence = true;
        this.isUserDragLineOnMap.off = true;
        let yunBd = this.editPolyline.ha || this.editPolyline.Ao;
        if (yunBd) {
          this.editPolyline.enableEditing();     // 开启编辑
        } else {
          this.$alert("百度坐标功能数组已更换。", "error：", {
            confirmButtonText: "确定",
          });
        }
      } else {
        this.isUserDragLineOnMap = {
          index: null,
          off: false
        }
        this.editPolyline.disableEditing();
      }
    },

    /**
     * 处理地图上鼠标不同模式下样式
     * @param editMode {Boolean} true:编辑模式  false:查看模式
     */
    changeUserMapCursor(editMode) {
      if (editMode) {
        if (!this.fenceTableData.length) {
          this.$notify({
            type: "info",
            message: "请先创建一个规划!",
          });
          this.isUserDrawOnMap = false;
        } else {
          let {name} = this.nowUserEditFence;
          if (name.trim() === "") {
            this.$notify({
              type: "info",
              message: "请在列表先开启一个规划编辑！",
            });
            this.isUserDrawOnMap = false;
          } else {
            this.dialogMap.setDefaultCursor("crosshair"); // 设置地图默认的鼠标指针样式    十字架型
            this.dialogMap.setDraggingCursor("crosshair"); // 设置地图拖拽时的鼠标指针样式
          }
        }
      } else {
        this.dialogMap.setDefaultCursor("pointer"); // 设置地图默认的鼠标指针样式    手型
        this.dialogMap.setDraggingCursor("pointer"); // 设置地图拖拽时的鼠标指针样式
        this.isUserDrawOnMap = false;
        this.userDrawPointList.forEach((item) => {
          this.dialogMap.removeOverlay(item);
        });
        this.userDrawPointList = [];
      }
    },

    /**
     * 删除围栏
     */
    del(index, row) {
      this.$confirm("此操作将永久删除该围栏, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let data = {
          id: row.id,
          token: this.token,
        };

        API.deleteExistFence(data).then((res) => {
          console.log(res)
          if (res.code) {
            this.$notify.error("删除围栏失败：" + res.msg);
            return;
          }

          this.$notify.success("删除围栏成功！");
          this.loadFenceList();
        }).catch(err => {
          this.$notify.error("删除围栏失败：" + err);
          console.log(err)
        });
      }).catch();
    },

    /**
     * 编辑已有围栏信息
     * @param index {Number} 当前围栏在列表中的索引
     * @param data {Object} 当前围栏的信息
     */
    edit(index, data) {
      this.nowUserEditFence.index = index;
      this.nowUserEditFence.name = data.fence_name;
      this.inputFenceName = data.fence_name;
      this.inputFenceType = data.type;
      if (data.type === 2) {
        this.inputFenceMaximumNumber = data.capacity;
      } else {
        this.inputFenceMaximumNumber = this.defaultMaximumNumber;
      }
      this.isAddFence = false;
      this.fenceDialogVisible = true;     // 打开围栏信息编辑框
    },

    /**
     * 处理围栏信息编辑
     */
    editTempFence() {
      this.changeUserMapCursor(false);             // 恢复鼠标原始状态
      // 信息预处理部分由于跳转限制 由上方edit()方法中处理

      this.createBaiduPolyline([this.fenceTableData[this.nowUserEditFence.index]], "dialog");        // 创建围栏实例
    },

    /**
     * 处理围栏信息编辑完成后 临时变量清空
     */
    removeEditDialogData() {
      this.inputFenceName = '';
      this.inputFenceType = '';
      this.inputFenceMaximumNumber = '';
      this.editPolyline = {};
      this.nowUserEditFence.index = -1;
      this.nowUserEditFence.name = '';
      this.hasEditFence = false;
      this.clearMapOverlay("dialog");
    },

    /**
     * 完成围栏信息的输入
     */
    finishInputFence() {
      if (this.isAddFence) {
        if (this.inputFenceName.trim() === '') {
          this.inputFenceName = this.nowUserEditFence.name;
          this.$message.warning('围栏名称已置入默认值，如需修改请完毕后再提交');
          return;
        }
        if (this.inputFenceType === '') {
          this.$message.warning('围栏类型不能为空');
          return;
        }
        if (this.fenceTableData[this.nowUserEditFence.index].points.length < 3) {
          this.$message.warning('围栏点数不能少于3个，无法形成最小完整围栏');
          return;
        }

        let data = {
          fence_name: this.inputFenceName,
          type: this.inputFenceType,
          fence_points: JSON.stringify(this.procNewFenceData()),
          token: this.token,
        };
        if (this.inputFenceType === 2) {
          data.capacity = this.inputFenceMaximumNumber;
          data.current_num = 0;
        }

        API.addNewFence(data).then((res) => {
          if (res.code) {
            this.$notify.error("添加围栏失败：" + res.msg);
            return;
          }

          this.removeEditDialogData();
          this.fenceDialogVisible = false;
          this.$notify.success("添加围栏成功！");
          this.loadFenceList();
        }).catch(err => {
          this.$notify.error("添加围栏失败：" + err);
          console.log(err)
        });
      } else {
        if (this.inputFenceName.trim() === '') {
          this.$message.warning('围栏名称不能为空');
          return;
        }
        if (this.inputFenceType === '') {
          this.$message.warning('围栏类型不能为空');
          return;
        }

        let data = {
          id: this.fenceTableData[this.nowUserEditFence.index].id,
          fence_name: this.inputFenceName,
          type: this.inputFenceType,
          fence_points: JSON.stringify(this.procEditFenceData()),
          token: this.token,
        };

        if (this.inputFenceType === 2) {
          data.capacity = this.inputFenceMaximumNumber;
          data.current_num = this.fenceTableData[this.nowUserEditFence.index].current_num;
        }
        API.updateExistFence(data).then((res) => {
          if (res.code) {
            this.$notify.error("更新围栏失败：" + res.msg);
            return;
          }

          this.removeEditDialogData();
          this.fenceDialogVisible = false;
          this.$notify.success("更新围栏成功！");
          this.loadFenceList();
        }).catch(err => {
          this.$notify.error("更新围栏失败：" + err);
          console.log(err)
        });
      }
    },

    /**
     * 处理编辑已有围栏信息
     */
    procEditFenceData() {
      if (this.hasEditFence) {
        // 创建一个新数组存储最新的qa=0的点
        let newPoints = [];

        // 遍历 this.editPolyline.Sc 来寻找qa=0的点
        this.editPolyline.Sc.forEach((point, index) => {
          if (point.qa === 0) {
            // 将这个点的坐标存入新数组
            newPoints.push(point.yb);
          }
        });

        // 更新 this.pointArr[this.theCurrent.index].points
        return newPoints;
      } else {
        return this.fenceTableData[this.nowUserEditFence.index].fence_points;
      }
    },

    /**
     * 处理添加新围栏信息
     */
    procNewFenceData() {
      let newPoints = [];

      this.fenceTableData[this.nowUserEditFence.index].points.forEach((point, index) => {
        newPoints.push(point);
      });

      return newPoints;
    },

    /**
     * 由添加围栏按钮触发的围栏信息输入
     */
    openFenceDialog() {
      this.isAddFence = true;
      this.fenceDialogVisible = true;
    },

    /**
     * 处理未提交的围栏信息编辑
     */
    handleClose() {
      if (this.isAddFence) {
        this.$confirm('退出后将不会保存当前编辑的围栏信息，确认退出吗？', '确认关闭？', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.changeUserMapCursor(false);
          // 删除对应pointArr中的数据
          this.fenceTableData.splice(this.nowUserEditFence.index, 1);
          this.removeEditDialogData();
          this.fenceDialogVisible = false;
        }).catch(() => {
        });
      } else {
        this.changeUserMapCursor(false);
        this.removeEditDialogData();
        this.fenceDialogVisible = false;
      }
    },
  }
}
</script>

<style scoped>
#map-container {
  width: 100%;
  height: 600px;
}

#dialog-map-container {
  width: 100%;
  height: 550px;
}

.card-map {
  width: calc(100% - 535px);
}

.right-table {
  margin-left: 15px;
  width: 520px;
  height: 640px;
  position: relative;
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

.dialog-title {
  font-size: 18px;
  font-weight: bold;
  color: #2a5ad7;
  margin-right: 15px;
  line-height: 40px;
}

.dialog-info-block {
  display: flex;
  margin-bottom: 15px;
  width: 100%;
  position: relative;
}

.switch-container {
  align-items: center;
  display: flex;
}

>>> .el-dialog__title {
  font-family: Microsoft YaHei;
  font-size: 20px;
  line-height: 30px;
  color: #2a5ad7;
  font-weight: bolder;
  opacity: 1;
}

>>> .el-dialog__header {
  border-bottom: 1px solid #ebebeb;
}

>>> .el-dialog__body {
  padding: 15px 20px 20px;
}

.button-row {
  display: flex;
  align-items: center;
}

.table-button {
  padding: 10px;
}

>>> .el-button--danger {
  margin-left: 5px;
}

>>> .el-table--scrollable-y .el-table__body-wrapper::-webkit-scrollbar {
  width: 6px;

  opacity: 1;
  background-color: white;
}

>>> .el-table--scrollable-y .el-table__body-wrapper::-webkit-scrollbar-track {
  width: 6px;
  opacity: 1;
  border-radius: 10px;
  background-color: #E4ECF5;
}

>>> .el-table--scrollable-y .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 10px;
  height: 30px;
  /*-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);*/
  background-color: #2a5ad7;
}

.chartOb {
  width: 100%;
  height: 600px;
}

>>> .chartOb .el-card__body {
  height: calc(100% - 40px);
  width: calc(100% - 40px);
}

.chartHx {
  width: 650px;
  height: 550px;
}

#fenceType {
  width: 100%;
  height: 100%;
}

#fenceType2Max {
  width: 100%;
  height: 100%;
}
</style>
