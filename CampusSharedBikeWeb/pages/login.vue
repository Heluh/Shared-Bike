<template>
  <!--<all_map_comp/>-->
  <div style="position:absolute;left:0;top:0;right:0;bottom: 0;background-color: #628cf6">
    <el-card
      style="background-color: #2a5ad7;border:none;border-radius:20px;position: absolute;left:15%;top:15%;right:15%;bottom: 18%">
      <div style="height:100%;width:100%;position:relative;display: flex;justify-content: space-between">
        <img style="margin-top: 5%;margin-left: 10%" src="../assets/image/login_bg.png">
        <login-widget style="margin-right: 15%;margin-top: 8%"></login-widget>
      </div>
    </el-card>
  </div>
</template>

<script>
import LoginWidget from "@/components/loginWidget";

export default {
  name: 'IndexPage',
  components: {LoginWidget},
}
</script>
