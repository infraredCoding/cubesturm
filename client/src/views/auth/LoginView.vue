<script setup lang="ts">
import api from '@/axios'
import router from '@/router'
import { ref } from 'vue'
import { RouterLink } from 'vue-router'

const loginData = ref({
  username: null,
  password: null,
})

const errors = ref([])

const submitForm = async () => {
  await api
    .post('auth/login', {
      username: loginData.value.username,
      password: loginData.value.password,
    })
    .then((res) => {
      console.log(res.data), localStorage.setItem('token', res.data.token)
      localStorage.setItem('username', res.data.username)

      router.push('/')
    })
    .catch((err) => {
      if (err.status == 401) {
        errors.value.push('Incorrect username or Password')
      }
    })
}
</script>

<template>
  <section class="w-3/4 h-full mx-auto flex flex-col">
    <div class="card bg-base-100 shadow-sm w-1/2 mx-auto mt-15">
      <div class="card-body my-6">
        <h1 class="text-3xl text-center">Cubesturm</h1>
        <h6 class="text-xl text-center">Login</h6>

        <div class="flex flex-col w-4/5 px-10 gap-5 mx-auto">
          <div class="alert alert-success" v-if="$route.query.new">
            User Registration Successful. Please Login
          </div>

          <div class="alert alert-error" v-for="(e, idx) in errors" :key="idx">
            {{ e }}
          </div>

          <div class="flex flex-col gap-2">
            <label>Username</label>
            <input
              v-model="loginData.username"
              class="input w-full"
              type="text"
              placeholder="username"
            />
          </div>

          <div class="flex flex-col gap-2">
            <label>Password</label>
            <input
              v-model="loginData.password"
              class="input w-full"
              type="password"
              placeholder="password"
            />
          </div>

          <button class="btn btn-primary" @click.prevent="submitForm">Login</button>
          <RouterLink to="/register">Don't have an account? Register</RouterLink>
        </div>
      </div>
    </div>
  </section>
</template>
