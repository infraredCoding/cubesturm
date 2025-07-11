<script setup></script>

<template>
  <section class="w-3/4 mx-auto min-h-screen">
    <h1 class="text-3xl py-10">Puzzle Details</h1>
    <div class="flex gap-10 w-full">
      <div class="flex flex-col w-full gap-5">
        <div
          class="card indicator bg-base-100 w-1/4 flex flex-col justify-center align-middle shadow-lg"
        >
          <div class="indicator-item indicator-top">
            <button class="btn btn-info rounded-3xl">Edit</button>
          </div>
          <div class="card-body my-5 flex flex-col gap-2 text-center">
            <span class="cubing-icon event-333 text-4xl"></span>
            <h3 class="text-2xl">3x3</h3>
            <div>
              <h4 class="text-lg">Main: Huameng TG</h4>
              <h4 class="text-lg mb-3">Goal: Sub 8</h4>
            </div>
          </div>
        </div>

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
