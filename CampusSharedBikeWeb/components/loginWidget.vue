<template>
  <div>
    <el-card>
      <p class="title">
        管理员登录
        <span style="display: inline-block; padding-left: 100%"></span>
      </p>

      <el-form :model="loginForm" :rules="loginRule" ref="loginForm" :label-position="labelPosition">
        <el-form-item class="login-item">
          <el-input type="userName" v-model="loginForm.userName" placeholder="账号"
                    @keyup.enter.native="PasswordLogin('loginForm')"></el-input>
        </el-form-item>
        <el-form-item prop="pwd" style="margin-top: 20px;margin-bottom: 20px">
          <el-input v-model="loginForm.pwd" placeholder="密码" type="password"
                    @keyup.enter.native="PasswordLogin('loginForm')"></el-input>
        </el-form-item>
        <el-form-item style="margin-top: 10px;margin-bottom: 10px">
          <!-- @click="toHomePage"-->
          <cli-button @click="PasswordLogin('loginForm')"
                      class="submitBtn" round>登 录
          </cli-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import cliButton from "~/components/base/cliButton";
import API from "../api";
import Cookies from "js-cookie";
import md5 from "js-md5";

export default {
  name: "loginWidget",
  components: {cliButton},
  data() {
    return {
      token: '',
      type: '',

      loadingLogin: false,
      labelPosition: 'left',
      loginForm: {
        userName: '',
        pwd: ''
      },
      loginRule: {
        userName: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
          //{ min: 3, max: 10, message: '长度在 3 到 16 个字符', trigger: 'blur' }
        ],
        pwd: [
          {required: true, message: '请输入密码', trigger: 'blur'}
          //{ min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    toFindPwd() {
      this.$router.push({path: '../findPwd'});
    },
    toSignUp() {
      this.$router.push({path: '../sign_up'});
    },

    /**
     * 用户登录
     * 接口：/user/login
     * */
    async PasswordLogin(loginForm) {
      this.$refs[loginForm].validate((valid) => {
        if (!valid) {
          return
        }
        this.loadingLogin = true
      });
      // 密码加密
      let pwd = md5(this.loginForm.pwd)
      console.log("密码", pwd)
      let data = {
        username: this.loginForm.userName,
        password: pwd
      }
      let result = -1
      // console.log(data.pwd)
      await API.requestLogin(data).then(res => {
        console.log(res)
        if (res.code) {
          this.$message({
            type: 'error',
            message: res.msg
          })
          this.loadingLogin = false;
          return
        }

        this.$message({
          type: 'success',
          message: '登录成功'
        })
        result = res
        Cookies.set('token', res.token)
        this.$router.push({path: '/'});
        return res
      })

      this.loadingLogin = false;
    },

  }

}
</script>

<style scoped>
.title {
  width: 80%;
  margin-left: 10%;
  text-align: center;
  height: 2.5rem;
  font-weight: bold;
  font-size: 15px;
}

.title:after {
  display: inline-block;
  width: 100%;
  content: '';
}

/*
按钮圆角
*/
.submitBtn {
  display: block;
  width: 140px;
  margin: 0 auto;
  border-radius: 25px;
  margin-bottom: 0px;
  font-size: 15px;
  background-color: #8ec2ff;
  transition: 1s;
  border-width: 0;
  text-align: center;
}
</style>
