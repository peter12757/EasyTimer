package main

import (
	_ "ProjectRoot/internal/logic"
	_ "ProjectRoot/internal/packed"

	"github.com/gogf/gf/v2/os/gctx"

	"ProjectRoot/internal/cmd"
)

func main() {
	cmd.Main.Run(gctx.New())
}
