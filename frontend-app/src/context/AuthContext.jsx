import { createContext, useContext, useState, useEffect } from 'react'
import api from '../api/axios'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const storedUser = localStorage.getItem('user')
    const storedToken = localStorage.getItem('accessToken')
    if (storedUser && storedToken) {
      setUser(JSON.parse(storedUser))
    }
    setLoading(false)
  }, [])

  const login = async (username, password) => {
    const response = await api.post('/auth/login', { username, password })
    const data = response.data
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
    const userData = {
      username: data.username,
      email: data.email,
      role: data.role,
    }
    localStorage.setItem('user', JSON.stringify(userData))
    setUser(userData)
    return data
  }

  const register = async (username, email, password) => {
    const response = await api.post('/auth/register', { username, email, password })
    const data = response.data
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
    const userData = {
      username: data.username,
      email: data.email,
      role: data.role,
    }
    localStorage.setItem('user', JSON.stringify(userData))
    setUser(userData)
    return data
  }

  const logout = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
    setUser(null)
  }

  const isAdmin = () => {
    return user?.role === 'ROLE_ADMIN'
  }

  const isAuthenticated = () => {
    return user !== null && localStorage.getItem('accessToken') !== null
  }

  return (
    <AuthContext.Provider
      value={{ user, loading, login, register, logout, isAdmin, isAuthenticated }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export default AuthContext
