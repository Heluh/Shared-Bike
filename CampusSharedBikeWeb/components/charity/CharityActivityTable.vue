<template>
  <div>
    <div class="table-title">{{ forNowDuration ? '当前开放捐赠活动' : '全部捐赠活动' }}</div>
    <el-table v-if="forNowDuration" v-loading="loading" :data="tableData" stripe
              :header-row-style="{'font-weight': 'bold', color: '#2a5ad7', 'font-family': 'Microsoft YaHei'}"
              border
              max-height="237px">
      <el-table-column prop="name" label="活动名称"/>
      <el-table-column prop="receive_loc_name" label="捐赠接收地点"/>
      <el-table-column label="活动时间">
        <template slot-scope="scope">
          <span>{{
              scope.row.last_forever ? '长期' : scope.row.end_date_str + '截止'
            }}</span>
        </template>
      </el-table-column>
      <el-table-column label="受捐赠数量" prop="bike_count"/>
      <el-table-column prop="operation" label="操作" width="105px">
        <template slot-scope="scope">
          <div class="button-row">
            <el-button round type="primary" class="table-button" @click="edit(scope.$index, scope.row)"
                       icon="el-icon-edit"/>
            <el-button round type="danger" class="table-button" @click="del(scope.$index, scope.row)"
                       icon="el-icon-delete"/>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-table v-else v-loading="loading" :data="tableData" stripe
              :header-row-style="{'font-weight': 'bold', color: '#2a5ad7', 'font-family': 'Microsoft YaHei'}"
              border>
      <el-table-column prop="name" label="活动名称"/>
      <el-table-column prop="receive_loc_name" label="捐赠接收地点"/>
      <el-table-column label="活动周期" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.last_forever ? '长期' : '限时' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" prop="start_date_str" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.last_forever ? '-' : scope.row.start_date_str }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" prop="end_date_str" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.last_forever ? '-' : scope.row.end_date_str }}</span>
        </template>
      </el-table-column>
      <el-table-column label="受捐赠数量" prop="bike_count" width="100"/>
      <el-table-column prop="operation" label="操作" width="105px">
        <template slot-scope="scope">
          <div class="button-row">
            <el-button round type="primary" class="table-button" @click="edit(scope.$index, scope.row)"
                       icon="el-icon-edit"/>
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

    <charity-info-dialog type="edit" ref="editDialog" @updateAllList="updateAllList" />
  </div>
</template>

<script>
import API from "~/api";
import Cookies from "js-cookie";
import CharityInfoDialog from "~/components/charity/CharityInfoDialog";

export default {
  name: "charityActivityTable",
  components: {CharityInfoDialog},
  props: {
    forNowDuration: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentPage: 1,                 // 当前页
      pageSize: 10,                    // 每页显示条数
      listTotal: 0,                   // 列表总数

      loading: true,
      tableData: [],
      token: Cookies.get("token"),
    }
  },
  mounted() {
    this.getCharityEventListData();
  },
  methods: {
    /**
     * 获取活动列表数据
     * 只可使用此方法获取数据
     */
    getCharityEventListData() {
      this.loading = true;
      if (this.forNowDuration) {
        this.getOpenCharityEventListData();
      } else {
        this.getAllCharityEventListData();
      }
    },

    /**
     * 获取全部开放的活动列表
     * private方法 不可直接调用
     */
    getAllCharityEventListData() {
      let data = {
        token: this.token,
        page: this.currentPage,
        size: this.pageSize,
      };

      API.getAllCharityEventList(data).then((res) => {
        if (res.code) {
          this.$notify.error("获取全部活动列表失败：" + res.msg);
          return;
        }

        res.list && res.list.length > 0 && res.list.forEach(item => {
          item.start_date_str = this.convertDateTimeToString(item.start_date, 1);
          item.end_date_str = this.convertDateTimeToString(item.end_date, 1);
        });

        this.tableData = res.list;
        this.listTotal = res.count;
        this.loading = false;
      }).catch(err => {
        this.$notify.error("处理全部活动列表失败：" + err);
        console.log(err)
      });
    },

    /**
     * 获取当前开放的活动列表
     * private方法 不可直接调用
     */
    getOpenCharityEventListData() {
      let data = {
        token: this.token,
        page: this.currentPage,
        size: this.pageSize,
      };

      API.getOpenCharityEventList(data).then((res) => {
        if (res.code) {
          this.$notify.error("获取当前开放的活动列表失败：" + res.msg);
          return;
        }

        res.list && res.list.length > 0 && res.list.forEach(item => {
          item.end_date_str = this.convertDateTimeToString(item.end_date, 3);
        });

        this.tableData = res.list;
        this.listTotal = res.count;
        this.loading = false;
      }).catch(err => {
        this.$notify.error("处理当前开放的活动列表失败：" + err);
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
        return '';
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
      this.$confirm("此操作将永久删除该活动, 是否继续?", "提示", {
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
            this.$notify.error("删除活动失败：" + res.msg);
            return;
          }

          this.$notify.success("删除活动成功！");
          this.getCharityEventListData();
          this.$emit("updateOppositeListData", this.forNowDuration);
        }).catch(err => {
          this.$notify.error("删除活动失败：" + err);
          console.log(err)
        });
      }).catch();
    },

    /**
     * 编辑已有活动信息
     * @param index {Number} 当前活动在列表中的索引
     * @param data {Object} 当前活动的信息
     */
    edit(index, data) {
      this.$refs.editDialog.showDialog(data.id);
    },

    updateAllList() {
      this.$emit('updateAllList');
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
  }
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
