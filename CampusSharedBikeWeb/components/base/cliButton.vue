<template>
  <div @click="click_button"
       :class="{'button_normal':size==='normal',
                  'button_large':size==='large',
                  'button_small':size==='small',
                  'button_tiny':size==='tiny',
                  'button_style':!disabled,
                  'button_style_not_allowed':disabled}"
       :style="{'background-color': (border ? '' : styleColor[type]),
                  'border-color': styleColor[type],
                  'color': (border ? styleColor[type] : '#ffffff')}">
    <slot></slot>
  </div>
</template>

<script>

export default {
  name: "button",
  props: {
    /**
     * 按钮类型
     * 非必需，默认primary
     * type == primary 主题色
     * type == warning 警告色（这里是 #ff5d5d）
     * type == other 其他，需要传自定义color
     */
    type: {
      type: String,
      default: 'primary'
    },
    /**
     * 是否禁用
     * 非必需，默认不禁用
     */
    disabled: {
      default: false
    },
    /**
     * 按钮颜色
     * 非必需，默认primaryColor
     * type == other 时自定义颜色生效
     */
    color: {
      type: String,
      default: '#2a5ad7'
    },
    /**
     * 非必需
     * border为true 显示边框，无背景色
     *      为false 有背景色
     */
    border: {
      default: false
    },
    /**
     * 按钮尺寸
     * 非必需，默认normal
     * normal: 高度35px，宽度自适应
     * large: 高度40px，宽度自适应
     * small: 高度35px，宽度35px，只有图标的正方形按钮
     * tiny:高度26px，宽度自适应，适用于列表中
     */
    size: {
      type: String,
      default: 'normal'
    },
  },
  data() {
    return {
      styleColor: {
        'primary': '#2a5ad7',
        'danger': '#ff5d5d',
        'light': '#628cf6',
        'other': this.color,
      }
    }
  },
  methods: {
    click_button() {
      if (!this.disabled) this.$emit('click');
    }
  }
}
</script>

<style scoped>
.button_normal {
  height: 35px;
  width: fit-content;
  width: -moz-fit-content;
  display: flex;
  align-items: center;
  justify-content: center;
  padding-left: 16px;
  padding-right: 16px;
  border: 1px solid #2a5ad7;
  border-radius: 4px;
}

.button_large {
  height: 40px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #2a5ad7;
  border-radius: 3px;
}

.button_small {
  height: 35px;
  width: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #2a5ad7;
  border-radius: 3px;
}

.button_tiny {
  height: 26px;
  width: fit-content;
  width: -moz-fit-content;
  display: flex;
  align-items: center;
  justify-content: center;
  padding-left: 12px;
  padding-right: 12px;
  border: 1px solid #2a5ad7;
  border-radius: 3px;
}

.button_style {
  color: white;
  font-size: 14px;
  cursor: pointer;
}

.button_style_not_allowed {
  background-color: #909399 !important;
  border-color: #909399 !important;
  color: white;
  font-size: 14px;
  cursor: no-drop;
}
</style>

