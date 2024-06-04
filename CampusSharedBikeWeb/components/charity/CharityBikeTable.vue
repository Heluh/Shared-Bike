<template>
  <div>
    <el-table v-loading="loading" :data="tableData" stripe
              :header-row-style="{'font-weight': 'bold', color: '#2a5ad7', 'font-family': 'Microsoft YaHei'}"
              border>
      <el-table-column prop="charity_event_name" label="活动名称"/>
      <el-table-column prop="bike_type" label="车辆类型">
        <template slot-scope="scope">
          <span>{{ bikeTypeOptions[scope.row.bike_type].label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入库时间" prop="in_time_str" width="200"/>
      <el-table-column v-if="type === 'bind'" label="投入运营时间" prop="bind_use_time_str" width="200"/>
      <el-table-column prop="operation" label="操作" width="105px">
        <template slot-scope="scope">
          <div class="button-row">
            <el-button v-if="type === 'in'" round type="success" class="table-button" @click="edit(scope.$index, scope.row)"
                       icon="el-icon-check"/>
            <el-button round type="danger" class="table-button" @click="del(scope.$index, scope.row)"
                       icon="el-icon-delete"/>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <div style="display: flex; margin-top: 10px">
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

    <el-dialog title="转移车辆至运营"
               :visible.sync="dialogFormVisible"
               width="50%"
               append-to-body
               @opened="prepareFenceData">
      <el-form>
        <el-form-item label="转入停车区">
          <el-select v-model="transferFenceId" class="longer-el-select" placeholder="请选择">
            <el-option
              v-for="item in fenceOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="bindCharityBike">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import API from "~/api";

export default {
  name: "CharityBikeTable",
  props: {
    type: {
      type: String,
      default: "in",
    },
  },
  data() {
    return {
      currentPage: 1,                 // 当前页
      pageSize: 10,                   // 每页显示条数
      listTotal: 0,                   // 列表总数

      loading: true,
      tableData: [],
      token: Cookies.get("token"),
      bikeTypeOptions: [
        {
          value: 0,
          label: '普通车',
        },
        {
          value: 1,
          label: '公路车',
        },
        {
          value: 2,
          label: '山地车',
        },
        {
          value: 3,
          label: '三轮车',
        },
      ],

      fenceOptions: [],
      transferFenceId: '',
      transferCharityBikeId: '',
      dialogFormVisible: false,
    }
  },
  mounted() {
    this.getCharityBikeListData();
  },
  methods: {
    /**
     * 获取活动列表数据
     * 只可使用此方法获取数据
     */
    getCharityBikeListData() {
      this.loading = true;
      if (this.type === 'bind') {
        this.getBindCharityBikeListData();
      } else {
        this.getInCharityBikeListData();
      }
    },

    /**
     * 获取全部开放的活动列表
     * private方法 不可直接调用
     */
    getBindCharityBikeListData() {
      let data = {
        token: this.token,
        page: this.currentPage,
        size: this.pageSize,
      };

      API.getBindCharityBikeList(data).then((res) => {
        if (res.code) {
          this.$notify.error("获取车辆列表失败：" + res.msg);
          return;
        }

        res.list && res.list.length > 0 && res.list.forEach(item => {
          item.in_time_str = this.convertDateTimeToString(item.in_time, 1);
          item.bind_use_time_str = this.convertDateTimeToString(item.bind_use_time, 1);
        });

        this.tableData = res.list;
        this.listTotal = res.count;
        this.loading = false;
      }).catch(err => {
        this.$notify.error("处理车辆列表失败：" + err);
        console.log(err)
      });
    },

    /**
     * 获取当前开放的活动列表
     * private方法 不可直接调用
     */
    getInCharityBikeListData() {
      let data = {
        token: this.token,
        page: this.currentPage,
        size: this.pageSize,
      };

      API.getInCharityBikeList(data).then((res) => {
        if (res.code) {
          this.$notify.error("获取车辆列表失败：" + res.msg);
          return;
        }

        res.list && res.list.length > 0 && res.list.forEach(item => {
          item.in_time_str = this.convertDateTimeToString(item.in_time, 1);
        });

        this.tableData = res.list;
        this.listTotal = res.count;
        this.loading = false;
      }).catch(err => {
        this.$notify.error("处理车辆列表失败：" + err);
        console.log(err)
      });
    },

    /**
     * 获取当前开放的活动列表
     * @param data 时间戳
     * @param type 类型  1：完整日期时间格式  2：只包含日期格式  3：时间差
     */
    convertDateTimeToString(data, type) {
      if (!data) return '-';

      // 使用自带的Date类进行转换
      let date = new Date(data);
      let now = new Date();

      if (type === 1) {
        // 返回完整日期时间格式：xxxx年xx月xx日 hh:mm:ss
        return `${date.getFullYear()}年${this.formatNumber(date.getMonth() + 1)}月${this.formatNumber(date.getDate())}日 ${this.formatNumber(date.getHours())}:${this.formatNumber(date.getMinutes())}:${this.formatNumber(date.getSeconds())}`;
      } else if (type === 2) {
        // 返回日期格式：xxxx年xx月xx日
        return `${date.getFullYear()}年${this.formatNumber(date.getMonth() + 1)}月${this.formatNumber(date.getDate())}日`;
      } else if (type === 3) {
        // 计算时间差
        let diff = Math.abs(date - now);
        let years = Math.floor(diff / (1000 * 60 * 60 * 24 * 365));
        let months = Math.floor(diff / (1000 * 60 * 60 * 24 * 30));
        let days = Math.floor(diff / (1000 * 60 * 60 * 24));
        let hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        let minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

        // 协商最大单位
        let remaining = '';
        if (years > 0) {
          remaining = `${years}年`;
        } else if (months > 0) {
          remaining = `${months}个月`;
        } else if (days > 0) {
          remaining = `${days}天`;
        } else if (hours > 0) {
          remaining = `${hours}小时`;
        } else if (minutes > 0) {
          remaining = `${minutes}分钟`;
        } else {
          return '很快';
        }

        return `${remaining}后`;
      } else {
        return '-';
      }
    },

    /**
     * 格式化数字 补齐两位数
     */
    formatNumber(num) {
      return num.toString().padStart(2, '0');
    },

    /**
     * 删除活动
     */
    del(index, row) {
      this.$confirm("此操作将永久删除该捐赠车辆, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let data = {
          id: row.id,
          token: this.token,
        };

        API.deleteCharityEvent(data).then((res) => {
          console.log(res)
          if (res.code) {
            this.$notify.error("删除捐赠车辆失败：" + res.msg);
            return;
          }

          this.$notify.success("删除捐赠车辆成功！");
          this.getCharityBikeListData();
        }).catch(err => {
          this.$notify.error("删除捐赠车辆失败：" + err);
          console.log(err)
        });
      }).catch();
    },

    prepareFenceData() {
      this.transferFenceId = '';
      this.fenceOptions = [];

      let data = {
        page: 1,
        size: 1000,
        token: this.token
      };

      API.getFenceList(data).then(res => {
        if (res.code) {
          this.$message.error("加载围栏列表失败：" + res.msg);
          return;
        }

        res.list && res.list.length > 0 && res.list.forEach(item => {
          if (item.type === 2) {
            item.label = item.fence_name + "（" + item.capacity + "）";
            this.fenceOptions.push({
              value: item.id,
              label: item.label,
            });
          }
        });
      }).catch(err => {
        console.log(err);
        this.$message.error("加载围栏列表失败：" + err);
      });
    },

    /**
     * 编辑已有活动信息
     * @param index {Number} 当前活动在列表中的索引
     * @param data {Object} 当前活动的信息
     */
    edit(index, data) {
      this.transferCharityBikeId = data.id;
      this.dialogFormVisible = true;
    },

    bindCharityBike() {
      let data = {
        bike_id: this.transferCharityBikeId,
        fence_id: this.transferFenceId,
        token: this.token
      };

      API.bindCharityBike(data).then(res => {
        if (res.code) {
          this.$message.error("绑定失败：" + res.msg);
          return;
        }

        this.$message.success("绑定成功！");
        this.transferCharityBikeId = '';
        this.dialogFormVisible = false;
        this.$emit("updateAllList");
      }).catch(err => {
        console.log(err);
        this.$message.error("绑定失败：" + err);
      });

      this.dialogFormVisible = false;
    },

    /**
     * 修改表格每页显示数量时
     */
    handleSizeChange(val) {
      this.pageSize = val;
      this.getCharityEventListData();
    },

    /**
     * 修改表格当前页时
     */
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getCharityEventListData();
    },
  },

}
</script>

<style scoped>
.table-title {
  font-size: 20px;
  font-weight: bold;
  color: #2a5ad7;
  margin-bottom: 20px;
}

.table-button {
  padding: 10px;
}

.longer-el-select {
  width: 60%;
  min-width: 300px;
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
</style>
