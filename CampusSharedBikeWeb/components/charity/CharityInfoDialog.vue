<template>
  <div>
    <el-dialog class="modify-dialog"
               :visible.sync="visible"
               :title="type === 'add' ? '添加捐赠活动' : '修改捐赠活动'"
               width="65%"
               append-to-body>
      <el-form ref="form" v-loading="loading" :model="form" label-width="100px">
        <el-form-item label="活动名称">
          <el-input v-model="form.name" placeholder="捐赠活动名称简洁清晰即可"></el-input>
        </el-form-item>
        <el-form-item label="捐赠接收地点">
          <el-input v-model="form.receive_loc_name" placeholder="校内地点最佳 建议设立标识牌"></el-input>
        </el-form-item>
        <el-form-item label="活动周期">
          <el-radio-group v-model="form.last_forever">
            <el-radio :label="true">长期</el-radio>
            <el-radio :label="false">限时</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="!form.last_forever" label="开始时间">
          <el-date-picker v-model="form.start_date" type="datetime"></el-date-picker>
        </el-form-item>
        <el-form-item v-if="!form.last_forever" label="结束时间">
          <el-date-picker v-model="form.end_date" type="datetime"></el-date-picker>
        </el-form-item>
        <el-form-item label="活动详情">
          <el-input type="textarea" autosize v-model="form.description"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import Cookies from 'js-cookie';
import API from "~/api";

export default {
  name: 'CharityInfoDialog',
  props: {
    type: {
      type: String,
      default: 'add',
    },
  },
  data() {
    return {
      visible: false,
      loading: false,
      token: Cookies.get('token'),
      form: {
        name: '',
        receive_loc_name: '',
        last_forever: false,
        start_date: '',
        end_date: '',
        description: '',
      },
    };
  },
  methods: {
    showDialog(id) {
      if (this.type === 'edit') {
        this.loading = true;
        this.loadExistData(id);
      }
      this.visible = true;
    },

    loadExistData(id) {
      let data = {
        id: id,
        token: this.token,
      };

      API.getOneCharityEvent(data).then((res) => {
        if (res.code) {
          this.$message.error(res.msg);
          this.loading = false;
          return;
        }

        this.form = res;
        this.loading = false;
      }).catch((err) => {
        this.$message.error(err);
        this.loading = false;
      });
    },

    submitForm() {
      let data = {
        token: this.token,
        name: this.form.name,
        receive_loc_name: this.form.receive_loc_name,
        last_forever: this.form.last_forever,
        start_date: this.form.start_date,
        end_date: this.form.end_date,
        description: this.form.description,
      };

      if (data.last_forever) {
        data.start_date = '';
        data.end_date = '';
      }

      if (this.type === 'add') {
        API.addCharityEvent(data).then((res) => {
          if (res.code) {
            this.$message.error(res.msg);
            return;
          }
          this.$message.success('添加新活动成功');
          this.resetForm();
          this.$emit('updateAllList');
        }).catch((err) => {
          this.$message.error(err);
          console.log(err);
        });
      } else {
        data.id = this.form.id;
        API.updateCharityEvent(data).then((res) => {
          if (res.code) {
            this.$message.error(res.msg);
            return;
          }
          this.$message.success('修改活动成功');
          this.resetForm();
          this.$emit('updateAllList');
        }).catch((err) => {
          this.$message.error(err);
          console.log(err);
        });
      }

    },

    resetForm() {
      this.visible = false;
      this.form.name = '';
      this.form.receive_loc_name = '';
      this.form.last_forever = false;
      this.form.start_date = '';
      this.form.end_date = '';
      this.form.description = '';
    },
  },
};
</script>

<style scoped>
>>> .el-dialog__header .el-dialog__title {
  font-weight: bold;
  color: #2a5ad7;
}

>>> .el-form-item .el-form-item__label {
  font-weight: bold;
  color: #2a5ad7;
}

</style>
