<script setup>
import api from '@/axios'
import router from '@/router'
import { RiAddLine, RiDeleteBinLine, RiEdit2Line } from '@remixicon/vue'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const newSlotInfo = ref({})
const trainingSlots = ref({})
const selectedSlot = ref({})

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
  await api
    .get(`training/slots`)
    .then((res) => {
      console.log(res)
      trainingSlots.value = res.data
    })
    .catch((err) => console.log(err))

  console.log(new Date().getDay())
})

const parseDay = (day) => {
  const days = {
    SUNDAY: 7,
    MONDAY: 1,
    TUESDAY: 2,
    WEDNESDAY: 3,
    THURSDAY: 4,
    FRIDAY: 5,
    SATURDAY: 6,
  }
  return days[day.toUpperCase()] ?? -1
}

function getPriorityBadgeClass(priority) {
  const map = {
    VERY_HIGH: 'badge badge-error', // red
    HIGH: 'badge badge-warning', // yellow
    MEDIUM: 'badge badge-accent', // purple/pink
    LOW: 'badge badge-info', // blue
    VERY_LOW: 'badge badge-success', // green
  }
  return map[priority.toUpperCase()] || 'badge'
}

const addSlot = async () => {
  await api
    .post(`training/slots`, newSlotInfo.value)
    .then((res) => document.getElementById('add_slot_modal')?.close())
    .catch((err) => console.log(err))
}

const updateSlot = async () => {
  await api
    .patch(`training/slots/${selectedSlot.value.id}`, selectedSlot.value)
    .then((res) => document.getElementById('edit_slot_modal')?.close())
    .catch((err) => console.log(err))
}

const deleteSlot = async () => {
  await api
    .delete(`training/slots/${selectedSlot.value.id}`)
    .then((res) => {
      document.getElementById('delete_slot_modal')?.close()
      location.reload()
    })
    .catch((err) => console.log(err))
}

const openEditModal = (slot) => {
  selectedSlot.value = slot
  document.querySelector('#edit_slot_modal')?.showModal()
}

const openDelModal = (slot) => {
  selectedSlot.value = slot
  document.querySelector('#delete_slot_modal')?.showModal()
}
</script>

<template>
  <section class="w-full px-10 mx-auto min-h-screen">
    <div class="py-10 flex justify-between">
      <h1 class="text-3xl">My Training</h1>
      <button class="btn btn-success" onclick="add_slot_modal.showModal()">
        <RiAddLine /> Add Slot
      </button>
    </div>

    <div class="grid grid-cols-7 gap-3 w-full items-start">
      <div
        class="flex flex-col gap-4"
        v-for="trainingDay in Object.keys(trainingSlots)"
        :key="trainingDay"
      >
        <div
          class="text-xl text-center bg-base-300 py-2 rounded-xl shadow-lg"
          :class="{ 'border border-emerald-400': new Date().getDay() == parseDay(trainingDay) }"
        >
          {{ trainingDay }}
        </div>
        <div
          class="card border border-slate-50/10 bg-base-300 hover:transition-all hover:scale-105 hover:translate-y--1 duration-300 cursor-pointer"
          v-for="slot in trainingSlots[trainingDay]"
          :key="slot.id"
        >
          <div class="card-body">
            <!-- <input type="checkbox" class="checkbox checkbox-md" /> -->
            <i :class="`cubing-icon ${puzzleTypes[slot.puzzle]?.icon} text-3xl text-center`"></i>

            <h4 class="card-title text-center mx-auto">{{ slot.title }}</h4>
            <div :class="`${getPriorityBadgeClass(slot.priority)} mx-auto`">
              {{ slot.priority }}
            </div>
            <h6 class="text-center">{{ slot.duration }}</h6>
            <div class="flex gap-1 mx-auto">
              <button @click="openEditModal(slot)" class="cursor-pointer">
                <RiEdit2Line size="27" class="hover:text-info px-1 py-1 transition-all rounded" />
              </button>
              <button @click="openDelModal(slot)" class="cursor-pointer">
                <RiDeleteBinLine
                  size="27"
                  class="hover:text-error px-1 py-1 transition-all rounded"
                />
              </button>
            </div>
            <dialog id="edit_slot_modal" class="modal">
              <div class="modal-box">
                <h3 class="text-lg font-bold">Edit Slot</h3>
                <form class="w-full flex flex-col gap-3">
                  <fieldset class="fieldset w-full">
                    <legend class="fieldset-legend">Puzzle Type</legend>
                    <select class="select w-full" v-model="selectedSlot.puzzle" disabled>
                      <option selected>Pick Puzzle Type</option>
                      <option v-for="opt in Object.keys(puzzleTypes)" :key="opt" :value="opt">
                        {{ puzzleTypes[opt].val }}
                      </option>
                    </select>
                  </fieldset>

                  <fieldset class="fieldset w-full">
                    <legend class="fieldset-legend">Title</legend>
                    <input
                      class="input w-full"
                      type="text"
                      placeholder="Do Slow Solves"
                      v-model="selectedSlot.title"
                    />
                  </fieldset>

                  <fieldset class="fieldset w-full">
                    <legend class="fieldset-legend">Priority</legend>
                    <select class="select w-full" v-model="selectedSlot.priority">
                      <option value="VERY_HIGH">Very High</option>
                      <option value="HIGH">High</option>
                      <option value="MEDIUM" selected>Medium</option>
                      <option value="LOW">Low</option>
                      <option value="VERY_LOW">Very Low</option>
                    </select>
                  </fieldset>

                  <fieldset class="fieldset w-full">
                    <legend class="fieldset-legend">Duration</legend>
                    <input
                      class="input w-full"
                      type="text"
                      placeholder="30 mins"
                      v-model="selectedSlot.duration"
                    />
                  </fieldset>

                  <fieldset class="fieldset w-full">
                    <legend class="fieldset-legend">Day Of Week</legend>
                    <select class="select w-full" v-model="selectedSlot.dayOfWeek">
                      <option value="SUNDAY">Sunday</option>
                      <option value="MONDAY">Monday</option>
                      <option value="TUESDAY">Tuesday</option>
                      <option value="WEDNESDAY">Wednesday</option>
                      <option value="THRUSDAY">Thursday</option>
                      <option value="FRIDAY">Friday</option>
                      <option value="SATURDAY">Saturday</option>
                    </select>
                  </fieldset>

                  <button class="btn btn-primary text-center" @click="updateSlot">
                    Save Changes
                  </button>
                </form>
              </div>
              <form method="dialog" class="modal-backdrop">
                <button>close</button>
              </form>
            </dialog>

            <dialog id="delete_slot_modal" class="modal">
              <div class="modal-box">
                <h3 class="text-lg font-bold">Delete Slot</h3>

                <p class="mb-2">Are you sure you want to delete {{ selectedSlot.title }}</p>

                <div class="flex gap-2">
                  <button class="btn btn-error text-center" @click.prevent="deleteSlot">
                    Yes, Confirm
                  </button>
                </div>
              </div>
              <form method="dialog" class="modal-backdrop">
                <button>close</button>
              </form>
            </dialog>
          </div>
        </div>
      </div>
    </div>

    <dialog id="add_slot_modal" class="modal">
      <div class="modal-box">
        <h3 class="text-lg font-bold">Add Training Slot</h3>
        <form class="w-full flex flex-col gap-3">
          <fieldset class="fieldset w-full">
            <legend class="fieldset-legend">Puzzle Type</legend>
            <select class="select w-full" v-model="newSlotInfo.puzzle">
              <option selected>Pick Puzzle Type</option>
              <option v-for="opt in Object.keys(puzzleTypes)" :key="opt" :value="opt">
                {{ puzzleTypes[opt].val }}
              </option>
            </select>
          </fieldset>

          <fieldset class="fieldset w-full">
            <legend class="fieldset-legend">Title</legend>
            <input
              class="input w-full"
              type="text"
              placeholder="Do Slow Solves"
              v-model="newSlotInfo.title"
            />
          </fieldset>

          <fieldset class="fieldset w-full">
            <legend class="fieldset-legend">Priority</legend>
            <select class="select w-full" v-model="newSlotInfo.priority">
              <option value="VERY_HIGH">Very High</option>
              <option value="HIGH">High</option>
              <option value="MEDIUM" selected>Medium</option>
              <option value="LOW">Low</option>
              <option value="VERY_LOW">Very Low</option>
            </select>
          </fieldset>

          <fieldset class="fieldset w-full">
            <legend class="fieldset-legend">Duration</legend>
            <input
              class="input w-full"
              type="text"
              placeholder="30 mins"
              v-model="newSlotInfo.duration"
            />
          </fieldset>

          <fieldset class="fieldset w-full">
            <legend class="fieldset-legend">Day Of Week</legend>
            <select class="select w-full" v-model="newSlotInfo.dayOfWeek">
              <option value="SUNDAY">Sunday</option>
              <option value="MONDAY">Monday</option>
              <option value="TUESDAY">Tuesday</option>
              <option value="WEDNESDAY">Wednesday</option>
              <option value="THRUSDAY">Thursday</option>
              <option value="FRIDAY">Friday</option>
              <option value="SATURDAY">Saturday</option>
            </select>
          </fieldset>

          <button class="btn btn-primary text-center" @click="addSlot">Add Slot</button>
        </form>
      </div>
      <form method="dialog" class="modal-backdrop">
        <button>close</button>
      </form>
    </dialog>
  </section>
</template>
