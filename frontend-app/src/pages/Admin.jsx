import { useState, useEffect } from 'react'
import api from '../api/axios'

function Admin() {
  const [products, setProducts] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [activeTab, setActiveTab] = useState('products')
  const [showForm, setShowForm] = useState(false)
  const [editingProduct, setEditingProduct] = useState(null)
  const [formData, setFormData] = useState({
    name: '', description: '', price: '', imageUrl: '', stock: '', categoryId: '', active: true,
  })
  const [message, setMessage] = useState({ text: '', type: '' })

  useEffect(() => {
    fetchData()
  }, [])

  const fetchData = async () => {
    try {
      const [productsRes, categoriesRes] = await Promise.all([
        api.get('/products', { params: { page: 0, size: 100 } }),
        api.get('/categories'),
      ])
      setProducts(productsRes.data.content || [])
      setCategories(categoriesRes.data || [])
    } catch (error) {
      console.error('Error fetching data:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }))
  }

  const handleSubmitProduct = async (e) => {
    e.preventDefault()
    setMessage({ text: '', type: '' })

    const payload = {
      name: formData.name,
      description: formData.description,
      price: parseFloat(formData.price),
      imageUrl: formData.imageUrl,
      stock: parseInt(formData.stock) || 0,
      categoryId: parseInt(formData.categoryId),
      active: formData.active,
    }

    try {
      if (editingProduct) {
        await api.put(`/products/${editingProduct.id}`, payload)
        setMessage({ text: 'Product updated successfully!', type: 'success' })
      } else {
        await api.post('/products', payload)
        setMessage({ text: 'Product created successfully!', type: 'success' })
      }
      setShowForm(false)
      setEditingProduct(null)
      resetForm()
      fetchData()
    } catch (error) {
      setMessage({
        text: error.response?.data?.message || 'Error saving product',
        type: 'error',
      })
    }
  }

  const handleEdit = (product) => {
    setEditingProduct(product)
    setFormData({
      name: product.name,
      description: product.description || '',
      price: product.price,
      imageUrl: product.imageUrl || '',
      stock: product.stock,
      categoryId: product.categoryId,
      active: product.active,
    })
    setShowForm(true)
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this product?')) return
    try {
      await api.delete(`/products/${id}`)
      setMessage({ text: 'Product deleted successfully!', type: 'success' })
      fetchData()
    } catch (error) {
      setMessage({ text: 'Error deleting product', type: 'error' })
    }
  }

  const resetForm = () => {
    setFormData({
      name: '', description: '', price: '', imageUrl: '', stock: '', categoryId: '', active: true,
    })
  }

  if (loading) return <div className="loading">Loading admin panel...</div>

  return (
    <div className="container">
      <h2 className="section-title">Admin Panel</h2>

      {message.text && (
        <div className={`alert alert-${message.type}`}>{message.text}</div>
      )}

      {/* Tabs */}
      <div className="admin-tabs">
        <button
          className={`tab-btn ${activeTab === 'products' ? 'active' : ''}`}
          onClick={() => setActiveTab('products')}
        >
          Products ({products.length})
        </button>
        <button
          className={`tab-btn ${activeTab === 'categories' ? 'active' : ''}`}
          onClick={() => setActiveTab('categories')}
        >
          Categories ({categories.length})
        </button>
      </div>

      {activeTab === 'products' && (
        <>
          <div className="admin-header">
            <button
              className="btn btn-primary"
              onClick={() => { setShowForm(!showForm); setEditingProduct(null); resetForm() }}
            >
              {showForm ? 'Cancel' : 'Add Product'}
            </button>
          </div>

          {showForm && (
            <form onSubmit={handleSubmitProduct} className="admin-form">
              <h3>{editingProduct ? 'Edit Product' : 'New Product'}</h3>
              <div className="form-row">
                <div className="form-group">
                  <label>Name</label>
                  <input name="name" value={formData.name} onChange={handleInputChange} required />
                </div>
                <div className="form-group">
                  <label>Price</label>
                  <input name="price" type="number" step="0.01" value={formData.price} onChange={handleInputChange} required />
                </div>
              </div>
              <div className="form-group">
                <label>Description</label>
                <textarea name="description" value={formData.description} onChange={handleInputChange} rows="3" />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Image URL</label>
                  <input name="imageUrl" value={formData.imageUrl} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                  <label>Stock</label>
                  <input name="stock" type="number" value={formData.stock} onChange={handleInputChange} />
                </div>
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Category</label>
                  <select name="categoryId" value={formData.categoryId} onChange={handleInputChange} required>
                    <option value="">Select category</option>
                    {categories.map((cat) => (
                      <option key={cat.id} value={cat.id}>{cat.name}</option>
                    ))}
                  </select>
                </div>
                <div className="form-group form-check">
                  <label>
                    <input type="checkbox" name="active" checked={formData.active} onChange={handleInputChange} />
                    Active
                  </label>
                </div>
              </div>
              <button type="submit" className="btn btn-primary">
                {editingProduct ? 'Update' : 'Create'}
              </button>
            </form>
          )}

          <table className="admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Category</th>
                <th>Active</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product) => (
                <tr key={product.id}>
                  <td>{product.id}</td>
                  <td>{product.name}</td>
                  <td>${product.price?.toFixed(2)}</td>
                  <td>{product.stock}</td>
                  <td>{product.categoryName}</td>
                  <td>{product.active ? 'Yes' : 'No'}</td>
                  <td>
                    <button className="btn btn-outline btn-xs" onClick={() => handleEdit(product)}>
                      Edit
                    </button>
                    <button className="btn btn-danger btn-xs" onClick={() => handleDelete(product.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}

      {activeTab === 'categories' && (
        <table className="admin-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Description</th>
              <th>Active</th>
            </tr>
          </thead>
          <tbody>
            {categories.map((cat) => (
              <tr key={cat.id}>
                <td>{cat.id}</td>
                <td>{cat.name}</td>
                <td>{cat.description}</td>
                <td>{cat.active ? 'Yes' : 'No'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

export default Admin
