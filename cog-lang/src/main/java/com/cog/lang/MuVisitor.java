// Generated from Mu.g4 by ANTLR 4.2.2

package com.cog.lang;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MuParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MuVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MuParser#log}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLog(@NotNull MuParser.LogContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#add12Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd12Expr(@NotNull MuParser.Add12ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#stat_block_a}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat_block_a(@NotNull MuParser.Stat_block_aContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#condition_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition_block(@NotNull MuParser.Condition_blockContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#mult13Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMult13Expr(@NotNull MuParser.Mult13ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#eq9Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq9Expr(@NotNull MuParser.Eq9ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#stringExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpr(@NotNull MuParser.StringExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#unary14Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary14Expr(@NotNull MuParser.Unary14ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#while_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_stat(@NotNull MuParser.While_statContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(@NotNull MuParser.ArgContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(@NotNull MuParser.BlockContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(@NotNull MuParser.ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#if_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stat(@NotNull MuParser.If_statContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#num_eq9Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNum_eq9Expr(@NotNull MuParser.Num_eq9ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(@NotNull MuParser.StatContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#and5Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd5Expr(@NotNull MuParser.And5ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(@NotNull MuParser.AssignmentContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(@NotNull MuParser.ParseContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#numericAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericAtom(@NotNull MuParser.NumericAtomContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#logicalAtom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAtom(@NotNull MuParser.LogicalAtomContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#gt10Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGt10Expr(@NotNull MuParser.Gt10ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#not14Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot14Expr(@NotNull MuParser.Not14ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(@NotNull MuParser.FunctionCallContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#stat_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat_block(@NotNull MuParser.Stat_blockContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(@NotNull MuParser.AtomContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#or6Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr6Expr(@NotNull MuParser.Or6ExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link MuParser#exit_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExit_loop(@NotNull MuParser.Exit_loopContext ctx);
}