<script setup>
import { onMounted, ref, watch, onUnmounted, computed, onBeforeUnmount } from 'vue'
import 'cubing/twisty'
import api from '@/axios'
import { useRoute } from 'vue-router'

const puzzleTypes = {
  P222: { val: '2x2x2', icon: 'event-222' },
  P333: { val: '3x3x3', icon: 'event-333' },
  P444: { val: '4x4x4', icon: 'event-444' },
  P555: { val: '5x5x5', icon: 'event-555' },
  P666: { val: '6x6x6', icon: 'event-666' },
  P777: { val: '7x7x7', icon: 'event-777' },
  P3BLD: { val: '3x3x3', icon: 'event-333bf' },
  FMC: { val: '3x3x3', icon: 'event-333fm' },
  OH: { val: '3x3x3', icon: 'event-333oh' },
  CLOCK: { val: 'clock', icon: 'event-clock' },
  MEGAMINX: { val: 'megaminx', icon: 'event-minx' },
  PYRAMINX: { val: 'pyraminx', icon: 'event-pyram' },
  SKEWB: { val: 'skewb', icon: 'event-skewb' },
  SQ1: { val: 'square1', icon: 'event-sq1' },
  P4BLD: { val: '4x4x4', icon: 'event-444bf' },
  P5BLD: { val: '5x5x5', icon: 'event-555bf' },
}

const route = useRoute()
const algSet = ref({})

const currentScramble = ref('')
const player = ref(null)

// timer logic
const running = ref(false)
const time = ref(0)
const ready = ref(false)
let interval = null
const times = ref([])

const getScramble = () => {
  const randomAlg =
    algSet.value.algorithms[Math.floor(Math.random() * algSet.value.algorithms.length)].algorithm
  const moves = randomAlg.replaceAll('"', '').trim().split(/\s+/)

  // Invert each move
  const invertedMoves = moves.reverse().map((move) => {
    if (move.endsWith("'")) return move.slice(0, -1) // R' -> R
    if (move.endsWith('2')) return move // R2 -> R2
    return move + "'" // R -> R'
  })

  let result = invertedMoves.join(' ')

  // Optionally add a random AUF
  if (Math.random() > 0.5) {
    const aufOptions = ['', 'U', "U'", 'U2']
    const randomAuf = aufOptions[Math.floor(Math.random() * aufOptions.length)]
    if (randomAuf) result += ' ' + randomAuf
  }

  currentScramble.value = result
  return result
}

watch(currentScramble, (newVal) => {
  if (player.value) {
    player.value.alg = newVal // ✅ set property directly
  }
})

function startTimer() {
  running.value = true
  const start = Date.now() - time.value
  interval = setInterval(() => {
    time.value = Date.now() - start
  }, 10)
}

function stopTimer() {
  clearInterval(interval)
  running.value = false
  // Save time
  times.value.unshift(time.value)
  localStorage.setItem(`algsetTimes-${route.params.algSetId}`, JSON.stringify(times.value))
}

let spaceHeld = false

function handleKeyDown(e) {
  if (e.code === 'Space') {
    if (!spaceHeld) {
      spaceHeld = true
      if (!running.value) {
        // Prepare
        ready.value = true
        time.value = 0
      } else {
        // Stop timer
        stopTimer()
        getScramble()
      }
    }
  }
}

function handleKeyUp(e) {
  if (e.code === 'Space') {
    if (spaceHeld) {
      spaceHeld = false
      if (!running.value && ready.value) {
        // Start timer on release
        ready.value = false
        startTimer()
      }
    }
  }
}

onMounted(async () => {
  await api
    .get(`training/algset/${route.params.algSetId}`)
    .then((res) => {
      algSet.value = res.data
      console.log(res.data)
      if (player.value) {
        player.value.puzzle = puzzleTypes[res.data.puzzle]?.val
      }
      getScramble()
    })
    .catch((err) => console.log(err))

  const handleKey = (e) => {
    if (e.code === 'Space') {
      e.preventDefault() // stop page from scrolling
      getScramble()
    }
  }
  window.addEventListener('keydown', handleKeyDown)
  window.addEventListener('keyup', handleKeyUp)

  const saved = localStorage.getItem(`algsetTimes-${route.params.algSetId}`)
  if (saved) times.value = JSON.parse(saved)

  // Cleanup on destroy
  onUnmounted(() => {
    window.removeEventListener('keydown', handleKeyDown)
    window.removeEventListener('keyup', handleKeyUp)
  })
})

onBeforeUnmount(() => {
  clearInterval(interval)
})

const formattedTime = computed(() => {
  const ms = time.value % 1000
  const totalSeconds = Math.floor(time.value / 1000)
  const seconds = totalSeconds % 60
  const minutes = Math.floor(totalSeconds / 60)
  if (minutes > 0) {
    return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}.${String(ms).padStart(2, '0')}`
  }
  return `${String(seconds).padStart(2, '0')}.${String(ms).padStart(2, '0')}`
})

function startStop() {
  if (running.value) {
    // Stop timer
    clearInterval(interval)
    running.value = false
    // Save the time to the list
    times.value.unshift(time.value)
    localStorage.setItem(`algsetTimes-${route.params.algSetId}`, JSON.stringify(times.value))
  } else {
    // Start timer
    running.value = true
    const start = Date.now() - time.value
    interval = setInterval(() => {
      time.value = Date.now() - start
    }, 10) // update every 10ms
  }
}

function formatTime(ms) {
  const seconds = Math.floor(ms / 1000) % 60
  const minutes = Math.floor(ms / 60000)
  const milliseconds = ms % 1000
  if (minutes > 0) {
    return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}.${String(milliseconds).padStart(2, '0')}`
  }
  return `${String(seconds).padStart(2, '0')}.${String(milliseconds).padStart(2, '0')}`
}
</script>

<template>
  <section class="w-full px-10 mx-auto min-h-screen">
    <div class="py-10 flex justify-between">
      <h1 class="text-3xl">Train {{ algSet?.title }}</h1>
    </div>

    <div class="flex flex-col gap-10">
      <div class="w-5/6 mx-auto text-center">
        <h1 class="text-2xl">{{ currentScramble }}</h1>

        <h2 class="text-5xl">{{ formattedTime }}</h2>
        <twisty-player
          class="mx-auto"
          ref="player"
          visualization="2D"
          background="none"
          hint-facelets="none"
          control-panel="none"
        ></twisty-player>

        <span class="inline-block mt-5"
          >Press <kbd class="kbd kbd-md">Space</kbd> to change scramble</span
        >

        <div class="bg-base-100 mt-5 py-2 rounded-2xl">
          Times: <br />
          <span v-for="(t, index) in times" :key="index">{{ formatTime(t) }}, </span>
        </div>
      </div>
    </div>
  </section>
</template>
