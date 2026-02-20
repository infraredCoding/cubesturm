<script setup>
import { onMounted, ref, watch, onBeforeUnmount, computed } from 'vue'
import videojs from 'video.js'
import 'video.js/dist/video-js.css'

import 'videojs-markers-plugin'

const videoPlayer = ref(null)
const video = ref(null)
const fileObject = ref(null)
const player = ref(null)
const notes = ref([])
const noteWriter = ref(null)

const handleFileUpload = (e) => {
  const file = e.target.files[0]
  if (file && file.type.startsWith('video/')) {
    // Create a local URL for the file
    video.value = URL.createObjectURL(file)
    fileObject.value = file

    if (player.value) {
      player.value.src({ type: file.type, src: video.value })
    }
  } else {
    video.value = null
    fileObject.value = null
    alert('Please select a valid video file.')
  }
}

onMounted(() => {
  if (videoPlayer.value) {
    player.value = videojs(videoPlayer.value, {
      controls: true,
      autoplay: false,
      preload: 'auto',
      fluid: true,
    })
  }
})

const addNote = () => {
  console.log(document.querySelector('#player').currentTime)
  notes.value.push({
    ts: document.querySelector('#player').currentTime,
    note: noteWriter.value,
  })
}

const parsedTime = (seconds) => {
  let rounded = parseInt(seconds)
  return `${Math.floor(rounded / 60)}:${String(rounded % 60).padStart(2, '0')}`
}

const goto = (seconds) => {
  document.querySelector('#player').currentTime = seconds
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
      <h1 class="text-3xl">Analyze Footage</h1>
    </div>

    <section class="flex">
      <div>
        <input
          type="file"
          class="file-input file-input-lg"
          @change="handleFileUpload"
          accept="video/*"
        />
      </div>
    </section>

    <section class="flex gap-10 justify-between w-full">
      <video
        id="player"
        ref="videoPlayer"
        controls
        v-if="video"
        :src="video"
        class="video-js self-start"
        style="width: 50%; height: auto"
      >
        Your browser does not support the video tag.
      </video>
      <div class="flex flex-col w-full">
        <div>
          <textarea
            class="textarea w-full mb-2"
            placeholder="Enter Note here"
            v-model="noteWriter"
          ></textarea>
          <button class="btn btn-accent" @click="addNote" :disabled="!video">Add Note</button>
        </div>

        <div class="mt-5 flex flex-col gap-4 overflow-y-scroll h-1/2">
          <div class="bg-base-100 card p-3 shadow-lg">
            <span class="badge badge-success mb-3">
              <button>3:10</button>
            </span>
            Too many rotations
          </div>

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
