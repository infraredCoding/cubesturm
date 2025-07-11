<script setup>
const props = defineProps(['name', 'main', 'icon', 'goal'])
</script>

<template>
  <div
    class="card bg-base-100 flex flex-col justify-center align-middle transition-all duration-300 hover:shadow-md hover:shadow-accent/20 hover:cursor-pointer hover:-translate-y-1.5"
  >
    <div class="card-body my-2 flex flex-col gap-2 text-center">
      <span :class="`cubing-icon ${props.icon} text-4xl`"></span>
      <h3 class="text-2xl">{{ props.name }}</h3>
      <div>
        <h4 class="text-lg">Main: {{ props.main }}</h4>
        <h4 class="text-lg mb-3">Goal: {{ props.goal }}</h4>
      </div>
      <!-- <router-link to="/" class="btn btn-info w-auto mx-auto">View More</router-link> -->
    </div>
  </div>
</template>
