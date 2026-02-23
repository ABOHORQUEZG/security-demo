import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import api from '../api/axios'

function ProductDetail() {
  const { id } = useParams()
  const [product, setProduct] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetchProduct()
  }, [id])

  const fetchProduct = async () => {
    try {
      const response = await api.get(`/products/${id}`)
      setProduct(response.data)
    } catch (err) {
      setError('Product not found')
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div className="loading">Loading product...</div>
  if (error) {
    return (
      <div className="container">
        <div className="error-page">
          <h2>{error}</h2>
          <Link to="/products" className="btn btn-primary">Back to Products</Link>
        </div>
      </div>
    )
  }
  if (!product) return null

  return (
    <div className="container">
      <Link to="/products" className="back-link">Back to Products</Link>

      <div className="product-detail">
        <div className="product-detail-image">
          <img
            src={product.imageUrl || 'https://via.placeholder.com/600x400?text=No+Image'}
            alt={product.name}
          />
        </div>
        <div className="product-detail-info">
          <span className="product-detail-category">{product.categoryName}</span>
          <h1>{product.name}</h1>
          <p className="product-detail-description">{product.description}</p>
          <div className="product-detail-meta">
            <span className="product-detail-price">${product.price?.toFixed(2)}</span>
            <span className={`product-detail-stock ${product.stock > 0 ? 'in-stock' : 'out-of-stock'}`}>
              {product.stock > 0 ? `${product.stock} in stock` : 'Out of stock'}
            </span>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ProductDetail
