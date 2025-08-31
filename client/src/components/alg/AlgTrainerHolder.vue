<script setup>
import router from '@/router'
import { RouterLink } from 'vue-router'

const props = defineProps(['name', 'puzzle', 'icon', 'algId'])
</script>

<template>
  <div
    class="card bg-base-100 flex flex-col justify-center align-middle transition-all duration-300 hover:shadow-md hover:cursor-pointer hover:-translate-y-1.5"
  >
    <div class="card-body my-2 flex flex-col gap-2 text-center">
      <span :class="`cubing-icon ${props.icon} text-4xl`"></span>
      <h3 class="text-2xl">{{ props.name }}</h3>
      <h3 class="text-xl">{{ props.puzzle }}</h3>

      <RouterLink
        class="btn btn-primary"
        :to="{ name: 'algset-trainer', params: { algSetId: props.algId } }"
        >Train</RouterLink
      >
    </div>
  </div>
</template>
