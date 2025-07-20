<script setup>
import api from '@/axios'
import PuzzleCard from '@/components/puzzles/PuzzleCard.vue'
import { onMounted, ref } from 'vue'

const puzzleTypes = {
  P222: { val: '2x2', icon: 'event-222' },
  P333: { val: '3x3', icon: 'event-333' },
  P444: { val: '4x4', icon: 'event-444' },
  P555: { val: '5x5', icon: 'event-555' },
  P666: { val: '6x6', icon: 'event-666' },
  P777: { val: '7x7', icon: 'event-777' },
  P3BLD: { val: '3BLD', icon: 'event-333bf' },
  FMC: { val: 'FMC', icon: 'event-333fm' },
  OH: { val: 'OH', icon: 'event-333oh' },
  CLOCK: { val: 'CLOCK', icon: 'event-clock' },
  MEGAMINX: { val: 'MEGAMINX', icon: 'event-minx' },
  PYRAMINX: { val: 'PYRAMINX', icon: 'event-pyram' },
  SKEWB: { val: 'SKEWB', icon: 'event-skewb' },
  SQ1: { val: 'SQ1', icon: 'event-sq1' },
  P4BLD: { val: '4BLD', icon: 'event-444bf' },
  P5BLD: { val: '5BLD', icon: 'event-555bf' },
  MBLD: { val: 'MBLD', icon: 'event-333mbf' },
}
const puzzles = ref([])

const newPuzzleData = ref({
  puzzle: 'P333',
  main: null,
})

onMounted(async () => {
  await api
    .get('events')
    .then((res) => {
      puzzles.value = res.data
      console.log(res.data)
    })
    .catch((err) => console.log(err))
})

const addPuzzle = async () => {
  await api
    .post('events', newPuzzleData.value, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    .then((res) => {
      puzzles.value.push(res.data)
      newPuzzleData.value.main = null
      newPuzzleData.value.puzzle = 'P333'
      document.getElementById('add_puzzle_modal').close()
    })
    .catch((err) => console.log(err))
}
</script>

<template>
  <section class="w-3/4 mx-auto min-h-screen">
    <h1 class="text-3xl py-10">Dashboard</h1>
    <div class="grid grid-cols-4 gap-4">
      <PuzzleCard
        v-for="p in puzzles"
        :name="puzzleTypes[p.puzzle]?.val || '3x3'"
        :puzzle="p"
        :icon="puzzleTypes[p.puzzle]?.icon"
        :key="p.id"
      />

      <div class="card bg-base-100 flex flex-col justify-center align-middle">
        <div class="card-body my-6">
          <button class="btn btn-success my-auto" onclick="add_puzzle_modal.showModal()">
            Add New Puzzle
          </button>

          <dialog id="add_puzzle_modal" class="modal">
            <div class="modal-box">
              <h3 class="text-lg font-bold">Add A New Puzzle</h3>
              <form class="w-full flex flex-col gap-3">
                <fieldset class="fieldset w-full">
                  <legend class="fieldset-legend">Puzzle Type</legend>
                  <select class="select w-full" v-model="newPuzzleData.puzzle">
                    <option selected>Pick Puzzle Type</option>
                    <option v-for="opt in Object.keys(puzzleTypes)" :key="opt" :value="opt">
                      {{ puzzleTypes[opt].val }}
                    </option>
                  </select>
                </fieldset>

                <fieldset class="fieldset w-full">
                  <legend class="fieldset-legend">Main</legend>
                  <input
                    class="input w-full"
                    type="text"
                    v-model="newPuzzleData.main"
                    placeholder="Some Cube Maglev"
                  />
                </fieldset>

                <button class="btn btn-primary text-center" @click.prevent="addPuzzle">
                  Add Puzzle
                </button>
              </form>
            </div>
            <form method="dialog" class="modal-backdrop">
              <button>close</button>
            </form>
          </dialog>
        </div>
      </div>
    </div>
  </section>
</template>
