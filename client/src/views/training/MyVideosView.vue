<script setup>
import { onMounted, ref } from 'vue'
import 'video.js/dist/video-js.css'

import 'videojs-markers-plugin'
import api from '@/axios'

const videos = ref([])
const newVideo = ref({
  title: '',
  file: null,
  duration: null,
})

const handleFileUpload = (event) => {
  newVideo.value.file = event.target.files[0]

  const video = document.createElement('video')
  video.preload = 'metadata'

  video.onloadedmetadata = () => {
    window.URL.revokeObjectURL(video.src)
    newVideo.value.duration = Math.round(video.duration)
  }

  video.src = URL.createObjectURL(newVideo.value.file)
}

const uploadFile = async () => {
  console.log(newVideo.value)
  const formData = new FormData()
  formData.append('title', newVideo.value.title)
  formData.append('file', newVideo.value.file)
  formData.append('duration', newVideo.value.duration)

  await api
    .post('videos/upload', formData, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    .then((res) => {
      videos.value.push(res.data)
      newVideo.value.file = null
      document.getElementById('add_video_modal').close()
    })
    .catch((err) => console.log(err))
}

onMounted(async () => {
  await api
    .get('videos')
    .then((res) => {
      videos.value = res.data
      console.log(res.data)
    })
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
      <button class="btn btn-success my-auto" onclick="add_video_modal.showModal()">
        Add Video
      </button>
    </div>

    <dialog id="add_video_modal" class="modal">
      <div class="modal-box">
        <h3 class="text-lg font-bold">Add A Video</h3>
        <form class="w-full flex flex-col gap-3">
          <fieldset class="fieldset w-full">
            <legend class="fieldset-legend">Video Title</legend>
            <input class="input w-full" v-model="newVideo.title" />
          </fieldset>

          <fieldset class="fieldset w-full">
            <legend class="fieldset-legend">Video File (mp4)</legend>
            <input
              class="input w-full file-input"
              type="file"
              accept="video/*"
              @change="handleFileUpload"
            />
          </fieldset>

          <button class="btn btn-primary text-center" @click.prevent="uploadFile">
            Upload Video
          </button>
        </form>
      </div>
      <form method="dialog" class="modal-backdrop">
        <button>close</button>
      </form>
    </dialog>

    <section class="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 w-full gap-5">
      <div
        class="card bg-neutral w-full hover:translate-x-0 cursor-pointer"
        @click="$router.push({ name: 'footage', params: { videoId: v.id } })"
        v-for="v in videos"
        :key="v.id"
      >
        <div class="card-body">
          <div class="w-full relative">
            <img :src="`http://localhost:8080/${v.thumbnail}`" class="w-full" />
            <span class="absolute bg-slate-950 text-amber-50 bottom-0 right-0 px-1">{{
              parsedTime(v.duration)
            }}</span>
          </div>
          <h1 class="text-2xl">{{ v.title }}</h1>
          <h6 class="text-md">Uploaded at: {{ new Date(v.creationDate).toLocaleString() }}</h6>
          <h6 class="text-md">Size: {{ parseSize(v.size) }} MB</h6>
        </div>
      </div>
    </section>
  </section>
</template>
