<template>
  <div>
    <div class="table-title">捐赠车辆信息录入</div>
    <el-divider/>
    <el-form ref="form" v-loading="loading" :model="form" label-width="100px">
      <el-form-item label="用户id">
        <el-input v-model="form.user_id"></el-input>
      </el-form-item>
      <el-form-item label="来自捐赠活动">
        <el-select v-model="form.charity_event_id" class="longer-el-select" placeholder="请选择">
          <el-option
            v-for="item in eventOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="车辆类型">
        <el-select v-model="form.bike_type" class="longer-el-select" placeholder="请选择">
          <el-option
            v-for="item in bikeTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import API from "~/api";

export default {
  name: "CharityBikeInForm",
  data() {
    return {
      loading: false,
      token: Cookies.get('token'),
      eventOptions: [],
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
      form: {
        user_id: '',
        charity_event_id: '',
        bike_type: '',
      },
    };
  },
  mounted() {
    this.loading = true;
    this.getOpenCharityEventListData();
  },
  methods: {
    /**
     * 获取当前开放的活动列表
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
          this.eventOptions.push({
            value: item.id,
            label: item.name,
          });
        });

        this.loading = false;
      }).catch(err => {
        this.$notify.error("处理当前开放的活动列表失败：" + err);
        console.log(err)
      });
    },

    submitForm() {
      this.loading = true;
      let data = {
        token: this.token,
        user_id: this.form.user_id,
        charity_event_id: this.form.charity_event_id,
        bike_type: this.form.bike_type,
      };

      API.addCharityBike(data).then((res) => {
        if (res.code) {
          this.$message.error(res.msg);
          return;
        }
        this.$message.success('捐赠车辆信息录入成功');
        this.resetForm();
        this.loading = false;
        this.$emit('updateAllList');
      }).catch((err) => {
        this.$message.error(err);
        console.log(err);
      });

    },

    resetForm() {
      this.form.user_id = '';
      this.form.charity_event_id = '';
      this.form.bike_type = '';
    },
  },
}
</script>

<style scoped>
.longer-el-select {
  width: 60%;
  min-width: 300px;
}

>>> .el-form-item .el-form-item__label {
  font-weight: bold;
  color: #2a5ad7;
}

.table-title {
  font-size: 20px;
  font-weight: bold;
  color: #2a5ad7;
}
</style>
