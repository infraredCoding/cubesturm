<script setup>
import { onMounted, ref } from 'vue'
import 'video.js/dist/video-js.css'

import 'videojs-markers-plugin'
import api from '@/axios'

const videos = ref([])

onMounted(async () => {
  await api
    .get('videos')
    .then((res) => (videos.value = res.data))
    .catch((err) => console.log(err))
})

const parsedTime = (seconds) => {
  let rounded = parseInt(seconds)
  return `${Math.floor(rounded / 60)}:${String(rounded % 60).padStart(2, '0')}`
}

const parseSize = (bytes) => {
  return (Number(bytes) / (1024 * 1024)).toFixed(1)
}
</script>

<template>
  <section class="w-full px-10 mx-auto min-h-screen">
    <div class="py-10 flex justify-between">
      <h1 class="text-3xl">Analyze Footage</h1>
    </div>

    <section class="grid grid-cols-4 w-full gap-5">
      <div
        class="card bg-neutral w-full hover:translate-x-0 cursor-pointer"
        @click="$router.push({ name: 'footage', params: { videoId: 1 } })"
        v-for="v in videos"
        :key="v.id"
      >
        <div class="card-body">
          <h1 class="text-2xl">{{ v.title }}</h1>
          <h6 class="text-md">Uploaded at: {{ new Date(v.creationDate).toLocaleString() }}</h6>
          <h6 class="text-md">Size: {{ parseSize(v.size) }} MB</h6>
        </div>
      </div>
    </section>
  </section>
</template>
