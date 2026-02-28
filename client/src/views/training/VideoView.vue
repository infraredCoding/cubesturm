<script setup>
import { onMounted, ref, watch, onBeforeUnmount, computed } from 'vue'
import videojs from 'video.js'
import 'video.js/dist/video-js.css'

import 'videojs-markers-plugin'
import api from '@/axios'
import { useRoute } from 'vue-router'

const route = useRoute()

const videoPlayer = ref(null)
const video = ref(null)
const player = ref(null)
const notes = ref([])
const noteWriter = ref(null)

// const handleFileUpload = (e) => {
//   const file = e.target.files[0]
//   if (file && file.type.startsWith('video/')) {
//     video.value = URL.createObjectURL(file)
//     fileObject.value = file

//     if (player.value) {
//       player.value.src({ type: file.type, src: video.value })
//     }
//   } else {
//     video.value = null
//     fileObject.value = null
//     alert('Please select a valid video file.')
//   }
// }

const loadVideoFromServer = async (videoId) => {
  player.value.src({
    type: 'video/mp4',
    src: `http://127.0.0.1:8080/api/v1/videos/${videoId}/stream`,
  })
}

onMounted(async () => {
  await api
    .get(`videos/${route.params.videoId}`)
    .then((res) => (video.value = res.data))
    .catch((err) => console.log(err))

  if (videoPlayer.value) {
    player.value = videojs(videoPlayer.value, {
      controls: true,
      autoplay: false,
      preload: 'auto',
      fluid: true,
    })
  }

  loadVideoFromServer(route.params.videoId)
})

const addNote = () => {
  console.log(player.value.currentTime())

  notes.value.push({
    ts: player.value.currentTime(),
    note: noteWriter.value,
  })
}

const parsedTime = (seconds) => {
  let rounded = parseInt(seconds)
  return `${Math.floor(rounded / 60)}:${String(rounded % 60).padStart(2, '0')}`
}

const goto = (seconds) => {
  player.value.currentTime(seconds)
}
onBeforeUnmount(() => {
  if (player.value) {
    player.value.dispose()
  }
})
</script>

<template>
  <section class="w-full px-10 mx-auto min-h-screen">
    <div class="py-10 flex justify-between">
      <h1 class="text-3xl">Analyze Footage: {{ video?.title }}</h1>
    </div>

    <section class="flex gap-10 justify-between w-full">
      <div class="w-full flex flex-col gap-5">
        <video crossorigin="anonymous" id="cvidplayer" ref="videoPlayer" controls class="video-js">
          Your browser does not support the video tag.
        </video>
        <div>
          <textarea
            class="textarea w-full mb-2"
            placeholder="Enter Note here"
            v-model="noteWriter"
          ></textarea>
          <button class="btn btn-accent" @click="addNote" :disabled="!noteWriter">Add Note</button>
        </div>
      </div>
      <div class="flex flex-col w-full overflow-y-auto max-h-[70vh]">
        <h1 class="text-2xl">Notes:</h1>
        <div
          class="bg-base-100 card px-5 text-center py-3 mt-2 shadow-lg cursor-pointer"
          v-if="notes.length < 1"
        >
          Your notes will be visible here
        </div>
        <div class="mt-5 flex flex-col gap-4">
          <div
            class="bg-base-100 card p-3 shadow-lg cursor-pointer"
            v-for="(note, idx) in notes"
            :key="idx"
            @click="goto(note.ts)"
          >
            <span class="badge badge-success mb-3"> {{ parsedTime(note.ts) }}</span>
            {{ note.note }}
          </div>
        </div>
      </div>
    </section>
  </section>
</template>
