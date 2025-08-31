<script setup>
import { onMounted, ref, watch, onUnmounted } from 'vue'

import { randomScrambleForEvent } from 'cubing/scramble'
import 'cubing/twisty'

const puzzleTypes = {
  222: { val: '2x2x2', icon: 'event-222' },
  333: { val: '3x3x3', icon: 'event-333' },
  444: { val: '4x4x4', icon: 'event-444' },
  555: { val: '5x5x5', icon: 'event-555' },
  666: { val: '6x6x6', icon: 'event-666' },
  777: { val: '7x7x7', icon: 'event-777' },
  clock: { val: 'clock', icon: 'event-clock' },
  minx: { val: 'megaminx', icon: 'event-minx' },
  pyra: { val: 'pyraminx', icon: 'event-pyram' },
  skewb: { val: 'skewb', icon: 'event-skewb' },
  sq1: { val: 'square1', icon: 'event-sq1' },
}

const selectedPuzzle = ref('333')

const currentScramble = ref('')
const player = ref(null)

const getScramble = async () => {
  console.log(selectedPuzzle.value)
  const scramble = await randomScrambleForEvent(selectedPuzzle.value)
  currentScramble.value = scramble.toString()
}

watch(currentScramble, (newVal) => {
  if (player.value) {
    player.value.alg = newVal // ✅ set property directly
  }
})

watch(selectedPuzzle, (newVal) => {
  console.log('aaaa', newVal)
  if (player.value) {
    player.value.puzzle = puzzleTypes[newVal]?.val // ✅ set property directly
  }
})

onMounted(() => {
  getScramble()

  const handleKey = (e) => {
    if (e.code === 'Space') {
      e.preventDefault() // stop page from scrolling
      getScramble()
    }
  }
  window.addEventListener('keydown', handleKey)

  // Cleanup on destroy
  onUnmounted(() => {
    window.removeEventListener('keydown', handleKey)
  })
})
</script>

<template>
  <section class="w-full px-10 mx-auto min-h-screen">
    <div class="py-10 flex justify-between">
      <h1 class="text-3xl">Slow Solves</h1>
    </div>

    <div class="flex flex-col gap-10">
      <div class="flex w-full justify-start">
        <div class="">
          <label>Puzzle</label>
          <select class="select" v-model="selectedPuzzle" @change="getScramble">
            <option v-for="opt in Object.keys(puzzleTypes)" :key="opt" :value="opt">
              {{ puzzleTypes[opt].val }}
            </option>
          </select>
        </div>
      </div>

      <div class="w-5/6 mx-auto text-center">
        <h1 class="text-2xl">{{ currentScramble }}</h1>
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
      </div>
    </div>
  </section>
</template>
