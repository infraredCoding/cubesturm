<script setup>
import { computed, ref } from 'vue'
import { useRoute, RouterView } from 'vue-router'
import BaseLayout from '@/layouts/BaseLayout.vue'

const route = useRoute()

const layout = computed(() => route.meta.layout || BaseLayout)
const isSidebarOpen = ref(true)

window.addEventListener('resize', () => {
  if (window.innerWidth < 768) {
    isSidebarOpen.value = false
  } else {
    isSidebarOpen.value = true
  }
})

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value
}
</script>

<template>
  <main class="w-full min-h-screen mix-w-screen h-screen pl-5 py-5 flex bg-base-200 font-display">
    <component :is="layout" :isSidebarOpen="isSidebarOpen">
      <RouterView :isSidebarOpen="isSidebarOpen" @toggleSidebar="toggleSidebar" />
    </component>
  </main>
</template>

<style scoped></style>
