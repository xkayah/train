import {createStore} from 'vuex'

const USER = "USER"

export default createStore({
    state: {
        user: window.SessionStorage.get(USER) || {}
    },
    getters: {},
    mutations: {
        setUser(state, _user) {
            state.user = _user
            window.SessionStorage.set(USER,_user)
        }
    },
    actions: {},
    modules: {}
})
