import {createStore} from 'vuex'

export default createStore({
    state: {
        user: {}
    },
    getters: {},
    mutations: {
        setUser(state, _user) {
            state.user = _user
        }
    },
    actions: {},
    modules: {}
})
