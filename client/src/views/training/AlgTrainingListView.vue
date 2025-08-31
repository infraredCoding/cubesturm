<script setup>
import api from '@/axios'
import AlgTrainerHolder from '@/components/alg/AlgTrainerHolder.vue'
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

const newAlgsetData = ref({
  puzzle: 'P333',
  name: null,
  file: null,
})

const handleFileUpload = (event) => {
  newAlgsetData.value.file = event.target.files[0]
}

onMounted(async () => {
  await api
    .get('training/algset')
    .then((res) => {
      puzzles.value = res.data
      console.log(res.data)
    })
    .catch((err) => console.log(err))
})

const uploadFile = async () => {
  const formData = new FormData()
  formData.append('puzzle', newAlgsetData.value.puzzle)
  formData.append('title', newAlgsetData.value.name)
  formData.append('file', newAlgsetData.value.file)

  // console.log(formData.get('title'))

  await api
    .post('training/algset', formData, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    .then((res) => {
      puzzles.value.push(res.data)
      newAlgsetData.value.file = null
      newAlgsetData.value.puzzle = 'P333'
      document.getElementById('add_puzzle_modal').close()
    })
    .catch((err) => console.log(err))
}
</script>

<template>
  <section class="w-3/4 mx-auto min-h-screen">
    <h1 class="text-3xl py-10">Alg Trainer</h1>
    <div class="grid grid-cols-4 gap-4">
      <AlgTrainerHolder
        v-for="p in puzzles"
        :algId="p.id"
        :name="p.title"
        :puzzle="puzzleTypes[p.puzzle]?.val || '3x3'"
        :icon="puzzleTypes[p.puzzle]?.icon"
        :key="p.id"
      />

      <div class="card bg-base-100 flex flex-col justify-center align-middle">
        <div class="card-body my-6">
          <button class="btn btn-success my-auto" onclick="add_puzzle_modal.showModal()">
            Add Algset
          </button>

          <dialog id="add_puzzle_modal" class="modal">
            <div class="modal-box">
              <h3 class="text-lg font-bold">Add A New Algset</h3>
              <form class="w-full flex flex-col gap-3">
                <fieldset class="fieldset w-full">
                  <legend class="fieldset-legend">Puzzle Type</legend>
                  <select class="select w-full" v-model="newAlgsetData.puzzle">
                    <option selected>Pick Puzzle Type</option>
                    <option v-for="opt in Object.keys(puzzleTypes)" :key="opt" :value="opt">
                      {{ puzzleTypes[opt].val }}
                    </option>
                  </select>
                </fieldset>

                <fieldset class="fieldset w-full">
                  <legend class="fieldset-legend">Algset Name</legend>
                  <input class="input w-full" v-model="newAlgsetData.name" />
                </fieldset>

                <fieldset class="fieldset w-full">
                  <legend class="fieldset-legend">CSV File</legend>
                  <input class="input w-full file-input" type="file" @change="handleFileUpload" />
                  <p>
                    The CSV File must contain a header for the column containing the algorithm named
                    "Alg", "Algs", "Algorithm", "Algorithms"
                  </p>
                </fieldset>

                <button class="btn btn-primary text-center" @click.prevent="uploadFile">
                  Upload Subset
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
