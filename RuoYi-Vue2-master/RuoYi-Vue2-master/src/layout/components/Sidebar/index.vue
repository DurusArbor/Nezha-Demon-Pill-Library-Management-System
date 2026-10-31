<template>
    <div :class="['sidebar-theme-wrapper', {'has-logo':showLogo}, settings.sideTheme]" :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
        <logo v-if="showLogo" :collapse="isCollapse" />
        <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
            <el-menu
                :default-active="activeMenu"
                :collapse="isCollapse"
                :background-color="settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground"
                :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
                :unique-opened="true"
                :active-text-color="settings.theme"
                :collapse-transition="false"
                mode="vertical"
            >
                <sidebar-item
                    v-for="(route, index) in visibleRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path"
                />
            </el-menu>
        </el-scrollbar>
    </div>
</template>

<script>
import { mapGetters, mapState } from "vuex"
import Logo from "./Logo"
import SidebarItem from "./SidebarItem"
import variables from "@/assets/styles/variables.scss"

export default {
    components: { SidebarItem, Logo },
    computed: {
        ...mapState(["settings"]),
        ...mapGetters(["sidebarRouters", "sidebar", "roles"]),
        // 按路由 meta.roles 过滤侧边栏菜单（未配置 roles 的菜单对所有角色可见）
        visibleRouters() {
            return this.filterByRoles(this.sidebarRouters)
        },
        activeMenu() {
            const route = this.$route
            const { meta, path } = route
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
                return meta.activeMenu
            }
            return path
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo
        },
        variables() {
            return variables
        },
        isCollapse() {
            return !this.sidebar.opened
        }
    },
    methods: {
        // 非破坏性过滤：返回克隆的路由数组，绝不能修改 constantRoutes 原始 children，
        // 否则切换账号重新登录后被过滤掉的子菜单会永久丢失
        filterByRoles(routes) {
            const roles = this.roles || []
            const result = []
            ;(routes || []).forEach(route => {
                if (route.meta && route.meta.roles && route.meta.roles.length) {
                    if (!roles.some(r => route.meta.roles.indexOf(r) !== -1)) {
                        return
                    }
                }
                const cloned = Object.assign({}, route)
                if (route.children && route.children.length) {
                    cloned.children = this.filterByRoles(route.children)
                }
                result.push(cloned)
            })
            return result
        }
    }
}
</script>
