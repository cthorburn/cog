// Generated from Mu.g4 by ANTLR 4.2.2

package com.cog.lang;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MuParser}.
 */
public interface MuListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MuParser#log}.
	 * @param ctx the parse tree
	 */
	void enterLog(@NotNull MuParser.LogContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#log}.
	 * @param ctx the parse tree
	 */
	void exitLog(@NotNull MuParser.LogContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#add12Expr}.
	 * @param ctx the parse tree
	 */
	void enterAdd12Expr(@NotNull MuParser.Add12ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#add12Expr}.
	 * @param ctx the parse tree
	 */
	void exitAdd12Expr(@NotNull MuParser.Add12ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#stat_block_a}.
	 * @param ctx the parse tree
	 */
	void enterStat_block_a(@NotNull MuParser.Stat_block_aContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#stat_block_a}.
	 * @param ctx the parse tree
	 */
	void exitStat_block_a(@NotNull MuParser.Stat_block_aContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#condition_block}.
	 * @param ctx the parse tree
	 */
	void enterCondition_block(@NotNull MuParser.Condition_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#condition_block}.
	 * @param ctx the parse tree
	 */
	void exitCondition_block(@NotNull MuParser.Condition_blockContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#mult13Expr}.
	 * @param ctx the parse tree
	 */
	void enterMult13Expr(@NotNull MuParser.Mult13ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#mult13Expr}.
	 * @param ctx the parse tree
	 */
	void exitMult13Expr(@NotNull MuParser.Mult13ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#eq9Expr}.
	 * @param ctx the parse tree
	 */
	void enterEq9Expr(@NotNull MuParser.Eq9ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#eq9Expr}.
	 * @param ctx the parse tree
	 */
	void exitEq9Expr(@NotNull MuParser.Eq9ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#stringExpr}.
	 * @param ctx the parse tree
	 */
	void enterStringExpr(@NotNull MuParser.StringExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#stringExpr}.
	 * @param ctx the parse tree
	 */
	void exitStringExpr(@NotNull MuParser.StringExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#unary14Expr}.
	 * @param ctx the parse tree
	 */
	void enterUnary14Expr(@NotNull MuParser.Unary14ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#unary14Expr}.
	 * @param ctx the parse tree
	 */
	void exitUnary14Expr(@NotNull MuParser.Unary14ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#while_stat}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stat(@NotNull MuParser.While_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#while_stat}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stat(@NotNull MuParser.While_statContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(@NotNull MuParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(@NotNull MuParser.ArgContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(@NotNull MuParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(@NotNull MuParser.BlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(@NotNull MuParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(@NotNull MuParser.ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#if_stat}.
	 * @param ctx the parse tree
	 */
	void enterIf_stat(@NotNull MuParser.If_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#if_stat}.
	 * @param ctx the parse tree
	 */
	void exitIf_stat(@NotNull MuParser.If_statContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#num_eq9Expr}.
	 * @param ctx the parse tree
	 */
	void enterNum_eq9Expr(@NotNull MuParser.Num_eq9ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#num_eq9Expr}.
	 * @param ctx the parse tree
	 */
	void exitNum_eq9Expr(@NotNull MuParser.Num_eq9ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(@NotNull MuParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(@NotNull MuParser.StatContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#and5Expr}.
	 * @param ctx the parse tree
	 */
	void enterAnd5Expr(@NotNull MuParser.And5ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#and5Expr}.
	 * @param ctx the parse tree
	 */
	void exitAnd5Expr(@NotNull MuParser.And5ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(@NotNull MuParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(@NotNull MuParser.AssignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(@NotNull MuParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(@NotNull MuParser.ParseContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#numericAtom}.
	 * @param ctx the parse tree
	 */
	void enterNumericAtom(@NotNull MuParser.NumericAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#numericAtom}.
	 * @param ctx the parse tree
	 */
	void exitNumericAtom(@NotNull MuParser.NumericAtomContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#logicalAtom}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAtom(@NotNull MuParser.LogicalAtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#logicalAtom}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAtom(@NotNull MuParser.LogicalAtomContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#gt10Expr}.
	 * @param ctx the parse tree
	 */
	void enterGt10Expr(@NotNull MuParser.Gt10ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#gt10Expr}.
	 * @param ctx the parse tree
	 */
	void exitGt10Expr(@NotNull MuParser.Gt10ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#not14Expr}.
	 * @param ctx the parse tree
	 */
	void enterNot14Expr(@NotNull MuParser.Not14ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#not14Expr}.
	 * @param ctx the parse tree
	 */
	void exitNot14Expr(@NotNull MuParser.Not14ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(@NotNull MuParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(@NotNull MuParser.FunctionCallContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#stat_block}.
	 * @param ctx the parse tree
	 */
	void enterStat_block(@NotNull MuParser.Stat_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#stat_block}.
	 * @param ctx the parse tree
	 */
	void exitStat_block(@NotNull MuParser.Stat_blockContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(@NotNull MuParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(@NotNull MuParser.AtomContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#or6Expr}.
	 * @param ctx the parse tree
	 */
	void enterOr6Expr(@NotNull MuParser.Or6ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#or6Expr}.
	 * @param ctx the parse tree
	 */
	void exitOr6Expr(@NotNull MuParser.Or6ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link MuParser#exit_loop}.
	 * @param ctx the parse tree
	 */
	void enterExit_loop(@NotNull MuParser.Exit_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuParser#exit_loop}.
	 * @param ctx the parse tree
	 */
	void exitExit_loop(@NotNull MuParser.Exit_loopContext ctx);
}