<script setup lang="ts">
import api from '@/axios'
import router from '@/router'
import { computed, ref } from 'vue'
import { RouterLink } from 'vue-router'

const regData = ref({
  name: null,
  email: null,
  wcaId: null,
  username: null,
  password: null,
  confirm_password: null,
})

const checkIfSame = computed(() => {
  const p1 = regData.value.password
  const p2 = regData.value.confirm_password
  if (p1 !== null && p2 != null && p1 !== '' && p2 !== '') {
    return p1 == p2
  }
  return true
})

const submitForm = async (e) => {
  e.preventDefault()
  if (!checkIfSame.value) return

  await api
    .post('auth/register', {
      name: regData.value.name,
      username: regData.value.username,
      email: regData.value.email,
      password: regData.value.password,
      wcaId: regData.value.wcaId,
    })
    .then((res) =>
      router.push({
        name: 'login',
        query: { new: 'true' },
      }),
    )
    .catch((err) => console.log(err))
}
</script>

<template>
  <section class="w-3/4 h-full mx-auto flex flex-col">
    <div class="card bg-base-100 shadow-sm w-1/2 mx-auto mt-15">
      <div class="card-body my-6">
        <h1 class="text-3xl text-center">Cubesturm</h1>
        <h6 class="text-xl text-center">Create an Account</h6>

        <div class="flex flex-col w-4/5 px-10 gap-5 mx-auto">
          <div class="flex flex-col gap-2">
            <label>Full Name</label>
            <input v-model="regData.name" class="input w-full" type="text" placeholder="Name" />
          </div>

          <div class="flex flex-col gap-2">
            <label>Email</label>
            <input
              v-model="regData.email"
              class="input w-full"
              type="email"
              placeholder="you@mail.com"
            />
          </div>

          <div class="flex flex-col gap-2">
            <label>Username</label>
            <input
              v-model="regData.username"
              class="input w-full"
              type="text"
              placeholder="username"
            />
          </div>

          <div class="flex flex-col gap-2">
            <label>WCA ID (Optional)</label>
            <input v-model="regData.wcaId" class="input w-full" type="text" placeholder="wca id" />
          </div>

          <div class="flex flex-col gap-2">
            <label>Password</label>
            <input
              v-model="regData.password"
              class="input w-full"
              type="password"
              placeholder="password"
            />
          </div>

          <div class="flex flex-col gap-2">
            <label>Confirm Password</label>
            <input
              v-model="regData.confirm_password"
              class="input w-full"
              type="password"
              placeholder="confirm password"
            />
          </div>

          <span class="text-error text-lg" v-if="!checkIfSame">Passwords do not match</span>

          <button class="btn btn-primary" @click.prevent="submitForm">Register</button>
          <RouterLink to="/login">Already have an account? Login</RouterLink>
        </div>
      </div>
    </div>
  </section>
</template>
