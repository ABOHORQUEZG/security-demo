import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import api from '../api/axios'

function Home() {
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchCategories()
  }, [])

  const fetchCategories = async () => {
    try {
      const response = await api.get('/categories')
      setCategories(response.data)
    } catch (error) {
      console.error('Error fetching categories:', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <div className="loading">Loading categories...</div>
  }

  return (
    <div className="home">
      <section className="hero">
        <div className="hero-content">
          <h1>Delicious Food, Delivered Fresh</h1>
          <p>
            Explore our wide range of cuisines and find your favorite meals.
            From pizzas to sushi, we have it all.
          </p>
          <Link to="/products" className="btn btn-primary btn-lg">
            Browse Menu
          </Link>
        </div>
      </section>

      <section className="container">
        <h2 className="section-title">Our Categories</h2>
        <div className="categories-grid">
          {categories.map((category) => (
            <Link
              key={category.id}
              to={`/products?category=${category.id}`}
              className="category-card"
            >
              <div className="category-card-image">
                <img
                  src={category.imageUrl || 'https://via.placeholder.com/400x250?text=Category'}
                  alt={category.name}
                  loading="lazy"
                />
              </div>
              <div className="category-card-body">
                <h3>{category.name}</h3>
                <p>{category.description}</p>
              </div>
            </Link>
          ))}
        </div>
      </section>
    </div>
  )
}

export default Home
