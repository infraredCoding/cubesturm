import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TrainingView from '@/views/training/TrainingView.vue'
import SlowSolvesView from '@/views/training/SlowSolvesView.vue'
import AlgTrainingListView from '@/views/training/AlgTrainingListView.vue'
import AlgTrainingView from '@/views/training/AlgTrainingView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true },
    },
    {
      path: '/training',
      name: 'training',
      component: TrainingView,
      meta: { requiresAuth: true },
    },
    {
      path: '/training/alg-trainer',
      name: 'alg-trainer',
      component: AlgTrainingListView,
      meta: { requiresAuth: true },
    },
    {
      path: '/training/alg-trainer/:algSetId',
      name: 'algset-trainer',
      component: AlgTrainingView,
      meta: { requiresAuth: true },
    },
    {
      path: '/slow-solves',
      name: 'slow-solves',
      component: SlowSolvesView,
      meta: { requiresAuth: true },
    },
    // auth stuff
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/auth/RegisterView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/auth/LoginView.vue'),
    },

    // Puzzles
    {
      path: '/puzzles/:puzzle',
      name: 'puzzle',
      component: () => import('../views/puzzles/PuzzleDetails.vue'),
      meta: { requiresAuth: true },
    },
  ],
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
