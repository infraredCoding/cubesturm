<script setup>
import api from '@/axios'
import router from '@/router'
import { RiDeleteBinLine, RiEdit2Line } from '@remixicon/vue'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const puzzleInfo = ref({})
const route = useRoute()

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

onMounted(async () => {
  console.log(this)
  await api
    .get(`events/${route.params.puzzle}`)
    .then((res) => (puzzleInfo.value = res.data))
    .catch((err) => console.log(err))
})

const savePuzzle = async () => {
  await api
    .patch(`events/${route.params.puzzle}`, puzzleInfo.value, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    .then((res) => document.getElementById('edit_puzzle_modal')?.close())
    .catch((err) => console.log(err))
}

const deletePuzzle = async () => {
  await api
    .delete(`events/${route.params.puzzle}`, puzzleInfo.value, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    .then((res) => router.push('/'))
    .catch((err) => console.log(err))
}
</script>

<template>
  <section class="w-3/4 mx-auto min-h-screen">
    <h1 class="text-3xl py-10">Puzzle Details - {{ puzzleTypes[puzzleInfo?.puzzle]?.val }}</h1>
    <div class="flex gap-10 w-full">
      <div class="flex flex-col w-full gap-5">
        <div
          class="card indicator bg-base-100 w-1/4 flex flex-col justify-center align-middle shadow-lg"
        >
          <div class="indicator-item indicator-top">
            <button class="btn btn-error rounded-3xl">
              <RiDeleteBinLine size="23" onclick="del_puzzle_modal.showModal()" />
            </button>
          </div>
          <div class="card-body my-5 flex flex-col gap-2 text-center">
            <span :class="`cubing-icon ${puzzleTypes[puzzleInfo?.puzzle]?.icon} text-4xl`"></span>
            <h3 class="text-2xl">{{ puzzleTypes[puzzleInfo?.puzzle]?.val }}</h3>
            <div class="text-left pl-[30%]">
              <h4 class="text-lg">Main: {{ puzzleInfo?.main }}</h4>
              <h4 class="text-lg mb-3">Goal: {{ puzzleInfo?.goal || 'N/A' }}</h4>
              <button class="btn btn-info" onclick="edit_puzzle_modal.showModal()">
                <ri-edit2-line />
                Edit Puzzle
              </button>
            </div>
          </div>
        </div>

        <dialog id="edit_puzzle_modal" class="modal">
          <div class="modal-box">
            <h3 class="text-lg font-bold">Edit Puzzle</h3>
            <form class="w-full flex flex-col gap-3">
              <fieldset class="fieldset w-full">
                <legend class="fieldset-legend">Puzzle Type</legend>
                <select class="select w-full" v-model="puzzleInfo.puzzle" disabled>
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
                  v-model="puzzleInfo.main"
                  placeholder="Some Cube Maglev"
                />
              </fieldset>

              <fieldset class="fieldset w-full">
                <legend class="fieldset-legend">Goal</legend>
                <input
                  class="input w-full"
                  type="text"
                  v-model="puzzleInfo.goal"
                  placeholder="Get sub 8"
                />
              </fieldset>

              <button class="btn btn-primary text-center" @click.prevent="savePuzzle">
                Save Changes
              </button>
            </form>
          </div>
          <form method="dialog" class="modal-backdrop">
            <button>close</button>
          </form>
        </dialog>

        <dialog id="del_puzzle_modal" class="modal">
          <div class="modal-box">
            <h3 class="text-lg font-bold">Delete Puzzle</h3>

            <p class="mb-2">Are you sure you want to delete puzzle {{ puzzleInfo.puzzle }}</p>

            <div class="flex gap-2">
              <button class="btn btn-error text-center" @click.prevent="deletePuzzle">
                Yes, Confirm
              </button>
              <button class="btn text-center" onclick="del_puzzle_modal.close()">
                Nope, cancel
              </button>
            </div>
          </div>
          <form method="dialog" class="modal-backdrop">
            <button>close</button>
          </form>
        </dialog>

        <div class="card bg-base-100 w-1/4 flex flex-col justify-center align-middle shadow-lg">
          <div class="card-body my-5 flex flex-col gap-2 text-center">
            <h3 class="text-2xl">PB Sheet</h3>
            <div>
              <h4 class="text-lg">Single: 6.78</h4>
              <h4 class="text-lg mb-3">ao5: 9.19</h4>
            </div>
            <!-- <router-link to="/" class="btn btn-info w-auto mx-auto">View More</router-link> -->
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
