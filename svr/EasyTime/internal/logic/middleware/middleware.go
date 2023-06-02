package middleware

import (
	"EasyTime/internal/service"

	"github.com/gogf/gf/v2/frame/g"
	"github.com/gogf/gf/v2/net/ghttp"
)

type (
	sMiddleware struct{}
)

func init() {
	service.RegisterMiddleware(New())
}

func New() service.IMiddleware {
	return &sMiddleware{}
}

func (s *sMiddleware) Auth(r *ghttp.Request) {
	g.Log("Middleware Auth")
	r.Middleware.Next()
}
