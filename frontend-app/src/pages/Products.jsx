import { useState, useEffect } from 'react'
import { useSearchParams } from 'react-router-dom'
import api from '../api/axios'
import ProductCard from '../components/ProductCard'

function Products() {
  const [searchParams] = useSearchParams()
  const [products, setProducts] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [keyword, setKeyword] = useState('')
  const [selectedCategory, setSelectedCategory] = useState(
    searchParams.get('category') || ''
  )

  useEffect(() => {
    fetchCategories()
  }, [])

  useEffect(() => {
    fetchProducts()
  }, [page, selectedCategory])

  const fetchCategories = async () => {
    try {
      const response = await api.get('/categories')
      setCategories(response.data)
    } catch (error) {
      console.error('Error fetching categories:', error)
    }
  }

  const fetchProducts = async () => {
    setLoading(true)
    try {
      let response
      if (selectedCategory) {
        response = await api.get(`/products/category/${selectedCategory}`, {
          params: { page, size: 9 },
        })
      } else {
        response = await api.get('/products', {
          params: { page, size: 9, sortBy: 'name', sortDir: 'asc' },
        })
      }
      setProducts(response.data.content || [])
      setTotalPages(response.data.totalPages || 0)
    } catch (error) {
      console.error('Error fetching products:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSearch = async (e) => {
    e.preventDefault()
    if (!keyword.trim()) {
      fetchProducts()
      return
    }
    setLoading(true)
    try {
      const response = await api.get('/products/search', {
        params: { keyword, page: 0, size: 9 },
      })
      setProducts(response.data.content || [])
      setTotalPages(response.data.totalPages || 0)
      setPage(0)
    } catch (error) {
      console.error('Error searching products:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleCategoryChange = (categoryId) => {
    setSelectedCategory(categoryId)
    setPage(0)
    setKeyword('')
  }

  return (
    <div className="container">
      <h2 className="section-title">Our Menu</h2>

      {/* Search Bar */}
      <form onSubmit={handleSearch} className="search-bar">
        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          placeholder="Search products..."
          className="search-input"
        />
        <button type="submit" className="btn btn-primary">
          Search
        </button>
      </form>

      {/* Category Filter */}
      <div className="category-filter">
        <button
          className={`filter-btn ${selectedCategory === '' ? 'active' : ''}`}
          onClick={() => handleCategoryChange('')}
        >
          All
        </button>
        {categories.map((cat) => (
          <button
            key={cat.id}
            className={`filter-btn ${selectedCategory === String(cat.id) ? 'active' : ''}`}
            onClick={() => handleCategoryChange(String(cat.id))}
          >
            {cat.name}
          </button>
        ))}
      </div>

      {/* Products Grid */}
      {loading ? (
        <div className="loading">Loading products...</div>
      ) : products.length === 0 ? (
        <div className="empty-state">
          <h3>No products found</h3>
          <p>Try adjusting your search or filter criteria.</p>
        </div>
      ) : (
        <>
          <div className="products-grid">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="pagination">
              <button
                className="btn btn-outline"
                onClick={() => setPage((p) => Math.max(0, p - 1))}
                disabled={page === 0}
              >
                Previous
              </button>
              <span className="pagination-info">
                Page {page + 1} of {totalPages}
              </span>
              <button
                className="btn btn-outline"
                onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                disabled={page >= totalPages - 1}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </div>
  )
}

export default Products
