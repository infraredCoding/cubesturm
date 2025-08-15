<script setup>
import router from '@/router'
import { useRoute } from 'vue-router'

const route = useRoute()

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push({ path: '/login' })
}

const props = defineProps(['isSidebarOpen'])
</script>

<template>
  <div
    class="bg-base-100 shadow-lg h-full flex flex-col rounded-2xl gap-5 pt-5 transition-all duration-150 relative"
    :class="props.isSidebarOpen ? 'w-[300px]' : 'w-0 overflow-hidden'"
  >
    <RouterLink
      to="/"
      class="text-md text-center rounded-2xl px-5 py-3 mx-5"
      :class="{ 'bg-accent text-white': route.path === '/' }"
      >Dashboard</RouterLink
    >

    <RouterLink
      to="/training"
      class="text-md text-center rounded-2xl px-5 py-3 mx-5"
      :class="{ 'bg-accent text-white': route.path === '/training' }"
      >Training</RouterLink
    >

    <button
      class="text-md text-center bg-slate-600/50 text-zinc-50 rounded-2xl px-5 py-3 mx-5 cursor-pointer"
      @click.prevent="logout()"
    >
      Logout
    </button>

    <span class="text-xs absolute bottom-2 pl-5"
      >Made with ❤️ by <a class="link" href="https://www.imraninfrared.com">infraredCoding</a></span
    >
  </div>
  <div class="flex-1 overflow-auto p-5">
    <slot />
  </div>
</template>
