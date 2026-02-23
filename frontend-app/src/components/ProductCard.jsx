import { Link } from 'react-router-dom'

function ProductCard({ product }) {
  return (
    <div className="product-card">
      <div className="product-card-image">
        <img
          src={product.imageUrl || 'https://via.placeholder.com/300x200?text=No+Image'}
          alt={product.name}
          loading="lazy"
        />
      </div>
      <div className="product-card-body">
        <span className="product-card-category">{product.categoryName}</span>
        <h3 className="product-card-title">{product.name}</h3>
        <p className="product-card-description">
          {product.description?.substring(0, 80)}
          {product.description?.length > 80 ? '...' : ''}
        </p>
        <div className="product-card-footer">
          <span className="product-card-price">${product.price?.toFixed(2)}</span>
          <Link to={`/products/${product.id}`} className="btn btn-primary btn-sm">
            View Details
          </Link>
        </div>
      </div>
    </div>
  )
}

export default ProductCard
